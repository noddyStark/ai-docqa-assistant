package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AnswerSourceResponse {

    private String sourceLabel;
    private UUID documentId;
    private String documentTitle;
    private Integer chunkIndex;
    private String previewText;
    private Double similarityScore;
}