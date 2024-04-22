package com.services.dog.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

@SuppressWarnings("unchecked")
@Slf4j
public class GlobalApiResponse<T> extends ResponseEntity<T> {
    public GlobalApiResponse(T data, HttpStatus status) {
        super(
                (T) new HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                    put("statusCode", status.value());
                    put("statusMessage", status.getReasonPhrase());
                    put("errors", null);
                    put("message", status.is2xxSuccessful() ? "Success" : status.getReasonPhrase());
                    put("data", data);
                }}
                , status);
    }

    public GlobalApiResponse(T data, HttpStatus status, String message) {
        super(
                (T) new HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                    put("statusCode", status.value());
                    put("statusMessage", status.getReasonPhrase());
                    put("errors", null);
                    put("message", message);
                    put("data", data);
                }}
                , status);
    }

    public GlobalApiResponse(HttpStatus status) {
        super(
                (T) new HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                    put("statusCode", status.value());
                    put("statusMessage", status.getReasonPhrase());
                    put("errors", null);
                    put("message", status.is2xxSuccessful() ? "Success" : status.getReasonPhrase());
                    put("data", null);
                }}
                , status);
    }

    public GlobalApiResponse(String message, HttpStatus status) {
        super(
                (T) new HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                    put("statusCode", status.value());
                    put("statusMessage", status.getReasonPhrase());
                    put("errors", null);
                    put("message", message);
                    put("data", null);
                }}
                , status);
    }

    public GlobalApiResponse(Object errors, String message, HttpStatus status) {
        super(
                (T) new HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                    put("statusCode", status.value());
                    put("statusMessage", status.getReasonPhrase());
                    put("errors", errors);
                    put("message", message == null ? status.getReasonPhrase() : message);
                    put("data", null);
                }}, status);
    }

    public String toJson() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this.getBody());
        } catch (JsonProcessingException e) {
            return "{}";
        }

    }
}