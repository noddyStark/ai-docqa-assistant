package com.shashank.docqa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngestDocumentRequest {

    private String title;
    private String sourceUrl;
    private String content;
}