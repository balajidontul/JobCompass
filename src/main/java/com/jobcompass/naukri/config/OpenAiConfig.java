package com.jobcompass.naukri.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class OpenAiConfig {

    @Bean
    public RestClient openAiRestClient(
            @Value("${openai.base-url}") String baseUrl,
            @Value("${openai.api-key}") String apiKey) {

        return RestClient.builder().
                baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer "+apiKey)
                .build();
    }
}