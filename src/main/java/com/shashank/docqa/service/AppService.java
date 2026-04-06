package com.shashank.docqa.service;

import org.springframework.stereotype.Service;

@Service
public class AppService {

    public String getStatusMessage() {
        return "DocQA Assistant is running!";
    }
}