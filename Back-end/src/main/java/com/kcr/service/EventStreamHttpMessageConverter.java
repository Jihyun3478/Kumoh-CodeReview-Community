package com.kcr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.io.IOException;

public class EventStreamHttpMessageConverter extends AbstractHttpMessageConverter<Object> {

    private final ObjectMapper objectMapper;

    public EventStreamHttpMessageConverter(ObjectMapper objectMapper) {
        super(MediaType.APPLICATION_JSON);
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // 이 메소드는 이 컨버터가 주어진 클래스를 지원하는지 여부를 결정합니다.
        return true; // 또는 특정 클래스를 지원하도록 조건을 추가
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        // 요청 본문을 Java 객체로 변환합니다.
        return objectMapper.readValue(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException {
        // Java 객체를 JSON으로 변환하여 응답 본문에 작성합니다.
        objectMapper.writeValue(outputMessage.getBody(), object);
    }
}
