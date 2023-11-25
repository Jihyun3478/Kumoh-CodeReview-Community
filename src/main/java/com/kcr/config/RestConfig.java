package com.kcr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcr.service.EventStreamHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    /* @Bean
     public RestTemplate restTemplate() {
         RestTemplate restTemplate = new RestTemplate();
         restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
         return restTemplate;
     }*/
    @Bean
    public RestTemplate restTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        // 사용자 정의 EventStreamHttpMessageConverter 추가
        restTemplate.getMessageConverters().add(new EventStreamHttpMessageConverter(objectMapper));

        return restTemplate;
    }
}
