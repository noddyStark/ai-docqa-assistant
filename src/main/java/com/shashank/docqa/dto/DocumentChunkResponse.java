package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DocumentChunkResponse {

    private UUID id;
    private UUID documentId;
    private Integer chunkIndex;
    private String chunkText;
    private LocalDateTime createdAt;
}