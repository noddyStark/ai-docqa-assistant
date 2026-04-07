package com.shashank.docqa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngestDocumentRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @Size(max = 1000, message = "Source URL must be at most 1000 characters")
    private String sourceUrl;

    @NotBlank(message = "Content is required")
    @Size(max = 200000, message = "Content must be at most 200000 characters")
    private String content;
}