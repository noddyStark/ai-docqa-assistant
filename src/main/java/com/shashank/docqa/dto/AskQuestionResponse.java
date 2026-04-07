package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AskQuestionResponse {
    private String question;
    private String answer;
    private List<AnswerSourceResponse> sources;
}