package com.shashank.docqa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DocumentLibraryResponse {

    private Integer totalDocuments;
    private List<DocumentLibraryItemResponse> documents;
}