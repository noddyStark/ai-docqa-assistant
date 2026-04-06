package com.shashank.docqa.service;

import com.shashank.docqa.dto.DocumentChunkResponse;
import com.shashank.docqa.dto.DocumentSummaryResponse;
import com.shashank.docqa.dto.IngestDocumentRequest;
import com.shashank.docqa.dto.IngestDocumentResponse;
import com.shashank.docqa.entity.Document;
import com.shashank.docqa.entity.DocumentChunk;
import com.shashank.docqa.repository.DocumentChunkRepository;
import com.shashank.docqa.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final ChunkingService chunkingService;
    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;

    public DocumentService(
            ChunkingService chunkingService,
            DocumentRepository documentRepository,
            DocumentChunkRepository documentChunkRepository
    ) {
        this.chunkingService = chunkingService;
        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
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
            DocumentChunk chunk = new DocumentChunk();
            chunk.setId(UUID.randomUUID());
            chunk.setDocumentId(documentId);
            chunk.setChunkIndex(i);
            chunk.setChunkText(chunks.get(i));
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
}