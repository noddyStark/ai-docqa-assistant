package com.shashank.docqa.controller;

import com.shashank.docqa.dto.*;
import com.shashank.docqa.service.DocumentService;
import com.shashank.docqa.service.QaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.shashank.docqa.dto.AskQuestionResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final QaService qaService;

    public DocumentController(DocumentService documentService, QaService qaService) {
        this.documentService = documentService;
        this.qaService = qaService;
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

    @PostMapping("/ask")
    public AskQuestionResponse askQuestion(@RequestBody AskQuestionRequest request) {
        return qaService.askQuestion(request.getQuestion());
    }
}