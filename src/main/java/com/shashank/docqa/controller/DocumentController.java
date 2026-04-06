package com.shashank.docqa.controller;

import com.shashank.docqa.dto.IngestDocumentRequest;
import com.shashank.docqa.dto.IngestDocumentResponse;
import com.shashank.docqa.service.DocumentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public IngestDocumentResponse ingest(@RequestBody IngestDocumentRequest request) {
        System.out.println("Inside DocumentController.ingest()");
        return documentService.ingest(request);
    }
}