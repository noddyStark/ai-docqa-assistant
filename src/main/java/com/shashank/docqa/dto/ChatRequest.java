package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRequest {
    private String model;
    private List<Message> messages;

    @Getter
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}