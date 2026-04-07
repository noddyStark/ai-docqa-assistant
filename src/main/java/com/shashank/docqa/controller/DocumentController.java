package com.shashank.docqa.controller;

import com.shashank.docqa.dto.*;
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

    @PostMapping("/search")
    public List<RetrievedChunkResponse> searchRelevantChunks(@RequestBody AskQuestionRequest request) {
        return documentService.retrieveRelevantChunks(request.getQuestion(), 3);
    }
}