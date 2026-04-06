package com.shashank.docqa.service;

import com.shashank.docqa.config.OpenAiProperties;
import com.shashank.docqa.dto.EmbeddingRequest;
import com.shashank.docqa.dto.EmbeddingResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EmbeddingService {

    private static final String OPENAI_EMBEDDINGS_URL = "https://api.openai.com/v1/embeddings";

    private final OpenAiProperties openAiProperties;
    private final RestTemplate restTemplate;

    public EmbeddingService(OpenAiProperties openAiProperties) {
        this.openAiProperties = openAiProperties;
        this.restTemplate = new RestTemplate();
    }

    public List<Double> generateEmbedding(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiProperties.getApi().getKey());

        EmbeddingRequest requestBody = new EmbeddingRequest(
                text,
                openAiProperties.getEmbedding().getModel()
        );

        HttpEntity<EmbeddingRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<EmbeddingResponse> response = restTemplate.exchange(
                OPENAI_EMBEDDINGS_URL,
                HttpMethod.POST,
                requestEntity,
                EmbeddingResponse.class
        );

        if (response.getBody() == null || response.getBody().getData() == null || response.getBody().getData().isEmpty()) {
            throw new RuntimeException("Failed to generate embedding: empty response from OpenAI");
        }

        return response.getBody().getData().get(0).getEmbedding();
    }
}