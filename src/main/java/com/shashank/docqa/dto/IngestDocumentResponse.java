package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class IngestDocumentResponse {

    private UUID documentId;
    private String status;
    private int chunksCreated;
    private List<String> chunks;
}