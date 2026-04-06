package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DocumentSummaryResponse {

    private UUID id;
    private String title;
    private String sourceUrl;
    private String contentType;
    private LocalDateTime createdAt;
}