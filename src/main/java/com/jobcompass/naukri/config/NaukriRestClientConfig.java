package com.jobcompass.naukri.config;

import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class NaukriRestClientConfig {

    @Bean
    public BasicCookieStore cookieStore() {
        return new BasicCookieStore();
    }

    @Bean
    public RestClient naukriRestClient(){

        CloseableHttpClient httpClient =
                HttpClients.custom().setDefaultCookieStore(cookieStore())
                        .build();
        HttpComponentsClientHttpRequestFactory requestFactory  =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        return RestClient.builder().requestFactory(requestFactory).build();
    }





}
