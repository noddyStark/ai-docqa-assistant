package com.shashank.docqa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "openai")
@Getter
@Setter
public class OpenAiProperties {

    private Api api = new Api();
    private Embedding embedding = new Embedding();
    private Chat chat = new Chat();

    @Getter
    @Setter
    public static class Api {
        private String key;
    }

    @Getter
    @Setter
    public static class Embedding {
        private String model;
    }

    @Getter
    @Setter
    public static class Chat {
        private String model;
    }
}