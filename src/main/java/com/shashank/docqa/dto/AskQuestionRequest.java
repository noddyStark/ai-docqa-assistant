package com.shashank.docqa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AskQuestionRequest {

    @NotBlank(message = "Question is required")
    @Size(max = 2000, message = "Question must be at most 2000 characters")
    private String question;
}