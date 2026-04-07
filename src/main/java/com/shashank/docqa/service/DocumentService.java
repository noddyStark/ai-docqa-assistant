package com.shashank.docqa.service;

import com.shashank.docqa.dto.*;
import com.shashank.docqa.entity.Document;
import com.shashank.docqa.entity.DocumentChunk;
import com.shashank.docqa.repository.DocumentChunkRepository;
import com.shashank.docqa.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final ChunkingService chunkingService;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final EmbeddingService embeddingService;

    public DocumentService(
            ChunkingService chunkingService,
            DocumentRepository documentRepository,
            DocumentChunkRepository documentChunkRepository,
            EmbeddingService embeddingService
    ) {
        this.chunkingService = chunkingService;
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.embeddingService = embeddingService;
    }

    @Transactional
    public IngestDocumentResponse ingest(IngestDocumentRequest request) {

        UUID documentId = UUID.randomUUID();
        List<String> chunks = chunkingService.chunkText(request.getContent());

        Document document = new Document();
        document.setId(documentId);
        document.setTitle(request.getTitle());
        document.setSourceUrl(request.getSourceUrl());
        document.setContentType("text/plain");
        document.setCreatedAt(LocalDateTime.now());

        documentRepository.save(document);

        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);

            List<Double> embeddingValues = embeddingService.generateEmbedding(chunkText);
            float[] embedding = toFloatArray(embeddingValues);

            DocumentChunk chunk = new DocumentChunk();
            chunk.setId(UUID.randomUUID());
            chunk.setDocumentId(documentId);
            chunk.setChunkIndex(i);
            chunk.setChunkText(chunkText);
            chunk.setEmbedding(embedding);
            chunk.setCreatedAt(LocalDateTime.now());

            documentChunkRepository.save(chunk);
        }

        return new IngestDocumentResponse(
                documentId,
                "INGESTED",
                chunks.size(),
                chunks
        );
    }

    public DocumentLibraryResponse getDocumentLibrary() {
        List<DocumentLibraryItemResponse> documents = documentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Document::getCreatedAt).reversed())
                .map(document -> new DocumentLibraryItemResponse(
                        document.getId(),
                        document.getTitle(),
                        document.getSourceUrl(),
                        document.getContentType(),
                        (int) documentChunkRepository.countByDocumentId(document.getId()),
                        document.getCreatedAt()
                ))
                .toList();

        return new DocumentLibraryResponse(documents.size(), documents);
    }

    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(document -> new DocumentSummaryResponse(
                        document.getId(),
                        document.getTitle(),
                        document.getSourceUrl(),
                        document.getContentType(),
                        document.getCreatedAt()
                ))
                .toList();
    }

    public DocumentDetailResponse getDocumentById(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));

        List<DocumentChunkSummaryResponse> chunkSummaries = documentChunkRepository
                .findByDocumentIdOrderByChunkIndexAsc(documentId)
                .stream()
                .map(chunk -> new DocumentChunkSummaryResponse(
                        chunk.getChunkIndex(),
                        buildPreview(chunk.getChunkText())
                ))
                .toList();

        return new DocumentDetailResponse(
                document.getId(),
                document.getTitle(),
                document.getSourceUrl(),
                document.getContentType(),
                chunkSummaries.size(),
                document.getCreatedAt(),
                chunkSummaries
        );
    }

    public List<DocumentChunkResponse> getChunksByDocumentId(UUID documentId) {
        return documentChunkRepository.findByDocumentIdOrderByChunkIndexAsc(documentId)
                .stream()
                .map(chunk -> new DocumentChunkResponse(
                        chunk.getId(),
                        chunk.getDocumentId(),
                        chunk.getChunkIndex(),
                        chunk.getChunkText(),
                        chunk.getCreatedAt()
                ))
                .toList();
    }

    public List<RetrievedChunkResponse> retrieveRelevantChunks(String question, int limit) {
        List<Double> queryEmbeddingValues = embeddingService.generateEmbedding(question);
        String queryEmbedding = toPgVector(queryEmbeddingValues);

        List<Object[]> rows = documentChunkRepository.findTopSimilarChunks(queryEmbedding, limit);

        List<RetrievedChunkResponse> results = new ArrayList<>();

        for (Object[] row : rows) {
            UUID chunkId = (UUID) row[0];
            UUID documentId = (UUID) row[1];
            Integer chunkIndex = (Integer) row[2];
            String chunkText = (String) row[3];
            double distance = ((Number) row[6]).doubleValue();

            double similarityScore = 1.0 - distance;

            results.add(new RetrievedChunkResponse(
                    chunkId,
                    documentId,
                    chunkIndex,
                    chunkText,
                    similarityScore
            ));
        }

        return results;
    }

    public String getDocumentTitle(UUID documentId) {
        return documentRepository.findById(documentId)
                .map(Document::getTitle)
                .orElse("Unknown Document");
    }

    public IngestDocumentResponse ingestFile(MultipartFile file, String sourceUrl) {
        validateFile(file);

        try {
            String filename = file.getOriginalFilename();
            String content;

            if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
                content = extractTextFromPdf(file);
            } else {
                content = new String(file.getBytes(), StandardCharsets.UTF_8);
            }

            IngestDocumentRequest request = new IngestDocumentRequest();
            request.setTitle(filename);
            request.setSourceUrl(sourceUrl);
            request.setContent(content);

            return ingest(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read uploaded file", e);
        }
    }

    @Transactional
    public void deleteDocument(UUID documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Document not found: " + documentId);
        }

        documentChunkRepository.deleteByDocumentId(documentId);
        documentRepository.deleteById(documentId);
    }

    private float[] toFloatArray(List<Double> values) {
        float[] result = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i).floatValue();
        }
        return result;
    }

    private String toPgVector(List<Double> values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if (i < values.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".txt") && !filename.endsWith(".md") && !filename.endsWith(".pdf"))) {
            throw new RuntimeException("Only .txt, .md, and .pdf files are supported");
        }
    }

    private String buildPreview(String text) {
        int maxLength = 200;

        if (text == null || text.isBlank()) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);

            if (text == null || text.isBlank()) {
                throw new RuntimeException("Could not extract readable text from PDF");
            }

            return text;
        }
    }
}