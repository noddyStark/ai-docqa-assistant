package com.shashank.docqa.service;

import com.shashank.docqa.dto.IngestDocumentRequest;
import com.shashank.docqa.dto.IngestDocumentResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final ChunkingService chunkingService;

    public DocumentService(ChunkingService chunkingService) {
        this.chunkingService = chunkingService;
    }

    public IngestDocumentResponse ingest(IngestDocumentRequest request) {

        UUID documentId = UUID.randomUUID();

        List<String> chunks = chunkingService.chunkText(request.getContent());
        for (int i = 0; i < chunks.size(); i++) {
            System.out.println("Chunk " + i + ": " + chunks.get(i));
        }

        return new IngestDocumentResponse(
                documentId,
                "INGESTED",
                chunks.size(),
                chunks
        );
    }
}