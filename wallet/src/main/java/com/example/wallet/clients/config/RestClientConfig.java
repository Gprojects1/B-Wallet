package com.example.wallet.clients.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpComponentsClientHttpRequestFactory requestFactory =
                    (HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory();
            requestFactory.setConnectTimeout(Duration.ofSeconds(10).toMillisPart());
            requestFactory.setReadTimeout(Duration.ofSeconds(10).toMillisPart());

        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, RestTemplateCustomizer restTemplateCustomizer) {
        return restTemplateBuilder.customizers(restTemplateCustomizer).build();
    }

}