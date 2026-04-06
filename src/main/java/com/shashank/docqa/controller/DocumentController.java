package com.shashank.docqa.controller;

import com.shashank.docqa.dto.DocumentChunkResponse;
import com.shashank.docqa.dto.DocumentSummaryResponse;
import com.shashank.docqa.dto.IngestDocumentRequest;
import com.shashank.docqa.dto.IngestDocumentResponse;
import com.shashank.docqa.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public IngestDocumentResponse ingest(@RequestBody IngestDocumentRequest request) {
        return documentService.ingest(request);
    }

    @GetMapping
    public List<DocumentSummaryResponse> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{documentId}/chunks")
    public List<DocumentChunkResponse> getChunksByDocumentId(@PathVariable UUID documentId) {
        return documentService.getChunksByDocumentId(documentId);
    }
}