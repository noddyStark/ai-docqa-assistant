package com.shashank.docqa.service;

import com.shashank.docqa.dto.*;
import com.shashank.docqa.entity.Document;
import com.shashank.docqa.entity.DocumentChunk;
import com.shashank.docqa.repository.DocumentChunkRepository;
import com.shashank.docqa.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private float[] toFloatArray(List<Double> values) {
        float[] result = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i).floatValue();
        }
        return result;
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
}