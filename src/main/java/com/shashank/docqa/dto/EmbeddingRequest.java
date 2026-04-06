package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmbeddingRequest {
    private String input;
    private String model;
}