package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RetrievedChunkResponse {

    private UUID chunkId;
    private UUID documentId;
    private Integer chunkIndex;
    private String chunkText;
    private Double similarityScore;
}