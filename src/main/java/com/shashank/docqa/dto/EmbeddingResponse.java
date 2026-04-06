package com.shashank.docqa.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmbeddingResponse {

    private List<EmbeddingData> data;

    @Getter
    @Setter
    public static class EmbeddingData {
        private List<Double> embedding;
        private Integer index;
    }
}