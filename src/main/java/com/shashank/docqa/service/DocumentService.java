package com.shashank.docqa.service;

import com.shashank.docqa.dto.IngestDocumentRequest;
import com.shashank.docqa.dto.IngestDocumentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class DocumentService {

    public IngestDocumentResponse ingest(IngestDocumentRequest request) {

        // For now, just simulate ingestion
        UUID documentId = UUID.randomUUID();

        int chunksCreated = simulateChunking(request.getContent());

        System.out.println("Inside DocumentService.ingest()");

        return new IngestDocumentResponse(
                documentId,
                "INGESTED",
                chunksCreated
        );
    }

    private int simulateChunking(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        // simple logic for now
        return Math.max(1, content.length() / 500);
    }
}