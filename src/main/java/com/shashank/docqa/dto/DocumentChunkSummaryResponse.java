package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DocumentChunkSummaryResponse {

    private Integer chunkIndex;
    private String previewText;
}