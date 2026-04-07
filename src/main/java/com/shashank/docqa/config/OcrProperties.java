package com.shashank.docqa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ocr.tesseract")
@Getter
@Setter
public class OcrProperties {
    private String datapath;
}