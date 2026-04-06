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
        int contentLength = normalizedContent.length();
        int start = 0;

        while (start < contentLength) {
            int tentativeEnd = Math.min(start + CHUNK_SIZE, contentLength);
            int end = findChunkEnd(normalizedContent, start, tentativeEnd);

            String chunk = normalizedContent.substring(start, end).trim();
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            if (end >= contentLength) {
                break;
            }

            int nextStart = Math.max(end - CHUNK_OVERLAP, 0);
            start = findChunkStart(normalizedContent, nextStart, contentLength);
        }

        return chunks;
    }

    private int findChunkEnd(String content, int start, int tentativeEnd) {
        if (tentativeEnd == content.length()) {
            return tentativeEnd;
        }

        int lastSpaceIndex = content.lastIndexOf(' ', tentativeEnd);

        if (lastSpaceIndex <= start) {
            return tentativeEnd;
        }

        return lastSpaceIndex;
    }

    private int findChunkStart(String content, int tentativeStart, int contentLength) {
        if (tentativeStart <= 0 || tentativeStart >= contentLength) {
            return tentativeStart;
        }

        if (content.charAt(tentativeStart) == ' ') {
            return tentativeStart + 1;
        }

        int nextSpaceIndex = content.indexOf(' ', tentativeStart);

        if (nextSpaceIndex == -1) {
            return tentativeStart;
        }

        return nextSpaceIndex + 1;
    }

    private String normalizeContent(String content) {
        return content.replaceAll("\\s+", " ").trim();
    }
}