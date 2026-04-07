package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DocumentDetailResponse {

    private UUID id;
    private String title;
    private String sourceUrl;
    private String contentType;
    private Integer chunkCount;
    private LocalDateTime createdAt;
    private List<DocumentChunkSummaryResponse> chunks;
}