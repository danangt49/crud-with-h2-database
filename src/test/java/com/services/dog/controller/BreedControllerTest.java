package com.services.dog.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BreedControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    private static final String URL = "http://localhost:7000/api/v1/breeds";

    @Test
    void list() {
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL+"/list")
                .build();

        log.info("GET List Data");
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> data = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("Response Body: {}", data.getBody());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void getBySubBreed() {
        String subBreed = "sheepdog";
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL+"/"+subBreed)
                .build();

        log.info("GET Data By Sub Breed");
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> data = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        log.info("Response Body: {}", data.getBody());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
    }
}