package com.shashank.docqa.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    private static final int CHUNK_SIZE = 500;
    private static final int CHUNK_OVERLAP = 100;

    public List<String> chunkText(String content) {
        List<String> chunks = new ArrayList<>();

        if (content == null || content.isBlank()) {
            return chunks;
        }

        String normalizedContent = normalizeContent(content);

        int start = 0;
        int contentLength = normalizedContent.length();

        while (start < contentLength) {
            int end = Math.min(start + CHUNK_SIZE, contentLength);
            String chunk = normalizedContent.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            if (end == contentLength) {
                break;
            }

            start = end - CHUNK_OVERLAP;
        }

        return chunks;
    }

    private String normalizeContent(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }
}