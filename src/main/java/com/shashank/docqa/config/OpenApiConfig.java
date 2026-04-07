package com.shashank.docqa.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI docQaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI-Powered Documentation Q&A Assistant API")
                        .description("Spring Boot + PostgreSQL + pgvector + OpenAI based RAG backend")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Shashank Shekhar")
                                .email("meshashank24@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/noddyStark/ai-docqa-assistant"));
    }
}