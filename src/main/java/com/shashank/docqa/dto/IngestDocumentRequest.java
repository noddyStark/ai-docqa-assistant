package com.shashank.docqa.dto;

public class IngestDocumentRequest {

    private String title;
    private String sourceUrl;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }
}