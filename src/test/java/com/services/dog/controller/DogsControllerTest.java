package com.services.dog.controller;

import com.google.gson.Gson;
import com.services.dog.vo.DogsQueryVo;
import com.services.dog.vo.DogsVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DogsControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    private static final String URL = "http://localhost:7000/api/v1/dogs";

    Long id = 1L;

    public static String generateRandomBreed() {
        Random random = new Random();
        String[] type = {"Retriever", "Shepherd", "Terrier", "Hound", "Spaniel", "Poodle", "Bulldog", "Boxer", "Collie", "Dalmatian"};
        return type[random.nextInt(type.length)];
    }

    public static List<String> generateRandomImageUrl(String breed) {
        return Collections.singletonList("https://www.example.com/images/dog/" + breed.toLowerCase() + ".jpg");
    }

    @Test
    void create() {
        String breed = generateRandomBreed();

        DogsVo req = new DogsVo();
        req.setBreed(breed);
        req.setImageUrls(generateRandomImageUrl(breed));

        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL)
                .build();

        log.info("POST Request Body: {}", req);
        HttpEntity<DogsVo> entity = new HttpEntity<>(req);
        ResponseEntity<String> data = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class
        );

        log.info("Response Body: {}", data.getBody());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    void page() {
        DogsQueryVo req = new DogsQueryVo("Terrier");
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL)
                .queryParam("q", req.getQ())
                .build();

        log.info("GET Request Add Param : {}", req);
        HttpEntity<DogsQueryVo> entity = new HttpEntity<>(req);
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
    void getById() {
        Assertions.assertNotNull(id, "ID is null");

        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL + "/" + id)
                .build();

        log.info("GET Data By ID: {}", id);
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
    void update() {
        Assertions.assertNotNull(id, "ID is null");

        String breed = generateRandomBreed();

        DogsVo req = new DogsVo();
        req.setBreed(breed);
        req.setImageUrls(generateRandomImageUrl(breed));

        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL + "/" + id)
                .build();

        log.info("GET Data By ID: {}", id);
        HttpEntity<DogsVo> entity = new HttpEntity<>(req);
        ResponseEntity<String> data = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.PUT,
                entity,
                String.class
        );

        log.info("Response Body: {}", data.getBody());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void delete() {
        Assertions.assertNotNull(id, "ID is null");

        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl(URL + "/" + id)
                .build();

        log.info("GET Data By ID: {}", id);
        HttpEntity<String> entity = new HttpEntity<>(null);
        ResponseEntity<String> data = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.DELETE,
                entity,
                String.class
        );

        log.info("Response Body: {}", data.getBody());
        Assertions.assertEquals(data.getStatusCode(), HttpStatus.OK);
    }
}
