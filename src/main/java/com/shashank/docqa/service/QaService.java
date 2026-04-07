package com.shashank.docqa.service;

import com.shashank.docqa.config.OpenAiProperties;
import com.shashank.docqa.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class QaService {

    private static final String OPENAI_CHAT_URL = "https://api.openai.com/v1/chat/completions";

    private final DocumentService documentService;
    private final OpenAiProperties openAiProperties;
    private final RestTemplate restTemplate;

    public QaService(DocumentService documentService, OpenAiProperties openAiProperties) {
        this.documentService = documentService;
        this.openAiProperties = openAiProperties;
        this.restTemplate = new RestTemplate();
    }

    public AskQuestionResponse askQuestion(String question) {
        List<RetrievedChunkResponse> retrievedChunks =
                documentService.retrieveRelevantChunks(question, 3);

        String prompt = buildPrompt(question, retrievedChunks);
        String answer = generateAnswer(prompt);

        List<AnswerSourceResponse> sources = buildSources(retrievedChunks);

        return new AskQuestionResponse(question, answer, sources);
    }

    private String buildPrompt(String question, List<RetrievedChunkResponse> chunks) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are a technical documentation assistant.\n");
        sb.append("Answer the user's question using only the provided context.\n");
        sb.append("If the answer is not contained in the context, say that you could not find it in the documentation.\n\n");

        sb.append("Question:\n");
        sb.append(question).append("\n\n");

        sb.append("Context:\n");
        for (int i = 0; i < chunks.size(); i++) {
            sb.append("[Source ").append(i + 1).append("]\n");
            sb.append(chunks.get(i).getChunkText()).append("\n\n");
        }

        return sb.toString();
    }

    private List<AnswerSourceResponse> buildSources(List<RetrievedChunkResponse> chunks) {
        List<AnswerSourceResponse> sources = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            RetrievedChunkResponse chunk = chunks.get(i);
            String documentTitle = documentService.getDocumentTitle(chunk.getDocumentId());

            sources.add(new AnswerSourceResponse(
                    "Source " + (i + 1),
                    chunk.getDocumentId(),
                    documentTitle,
                    chunk.getChunkIndex(),
                    buildPreview(chunk.getChunkText()),
                    chunk.getSimilarityScore()
            ));
        }

        return sources;
    }

    private String buildPreview(String text) {
        int maxLength = 200;

        if (text == null || text.isBlank()) {
            return "";
        }

        if (text.length() <= maxLength) {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }

    private String generateAnswer(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiProperties.getApi().getKey());

        ChatRequest request = new ChatRequest(
                openAiProperties.getChat().getModel(),
                List.of(
                        new ChatRequest.Message("system", "You are a helpful technical documentation assistant."),
                        new ChatRequest.Message("user", prompt)
                )
        );

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ChatResponse> response = restTemplate.exchange(
                OPENAI_CHAT_URL,
                HttpMethod.POST,
                entity,
                ChatResponse.class
        );

        if (response.getBody() == null
                || response.getBody().getChoices() == null
                || response.getBody().getChoices().isEmpty()) {
            throw new RuntimeException("Failed to generate answer: empty response from OpenAI");
        }

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}