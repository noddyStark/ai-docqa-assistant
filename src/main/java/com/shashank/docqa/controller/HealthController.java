package com.shashank.docqa.controller;

import com.shashank.docqa.service.AppService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller receives HTTP request
 * service contains business logic
 */
@RestController
public class HealthController {

    private final AppService appService;

    public HealthController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/health")
    public String health() {
        return appService.getStatusMessage();
    }
}
