package com.jobcompass.naukri.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class OpenAiService {

    private final RestClient openAiRestClient;
    private final String model;

    public OpenAiService(
            @Qualifier("openAiRestClient") RestClient openAiRestClient,
            @Value("${openai.model}") String model) {

        this.openAiRestClient = openAiRestClient;
        this.model = model;
    }

    public String ask(String prompt) {
        return openAiRestClient.post()
                .uri("/responses")
                .body(Map.of(
                        "model", model,
                        "input", prompt
                ))
                .retrieve()
                .body(String.class);
    }
}