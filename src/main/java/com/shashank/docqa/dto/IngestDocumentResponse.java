package com.shashank.docqa.dto;

import java.util.UUID;

public class IngestDocumentResponse {

    private UUID documentId;
    private String status;
    private int chunksCreated;

    public IngestDocumentResponse(UUID documentId, String status, int chunksCreated) {
        this.documentId = documentId;
        this.status = status;
        this.chunksCreated = chunksCreated;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public String getStatus() {
        return status;
    }

    public int getChunksCreated() {
        return chunksCreated;
    }
}