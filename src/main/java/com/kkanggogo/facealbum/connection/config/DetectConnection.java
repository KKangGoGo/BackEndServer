package com.kkanggogo.facealbum.connection.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@RequiredArgsConstructor
public class DetectConnection {

    @Value("${connectionbaseurl}")
    private String baseUrl;
    private final WebClient.Builder webClientBuild;

    @Bean
    public WebClient getWebClient(){
        return webClientBuild.baseUrl(baseUrl).build();
    }
}
