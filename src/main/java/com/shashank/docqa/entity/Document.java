package com.shashank.docqa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class Document {

    @Id
    private UUID id;

    private String title;
    private String sourceUrl;
    private String contentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}