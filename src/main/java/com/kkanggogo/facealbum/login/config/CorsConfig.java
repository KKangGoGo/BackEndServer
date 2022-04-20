package com.kkanggogo.facealbum.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //내 서버가 응답을 할 때 json을 js에서 처리 할수 있게 하겠다.
        config.addAllowedOriginPattern(CorsConfiguration.ALL); //모든 ip에 응답을 하겠다.
        config.addAllowedHeader("*"); //모든 header에 응답을 하겠다.
        config.addAllowedMethod("*"); //모든 post,get,put,delete,patch요청을 허용
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
