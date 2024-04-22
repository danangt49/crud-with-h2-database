package com.services.dog.config.httpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RestClient {
    @Value("${breeds.url}")
    private String baseUrl;
    private final RestTemplate restTemplate;
    private final RestTemplate restTemplateSub;

    @Autowired
    public RestClient(@Qualifier("allBreedsRestTemplate") RestTemplate restTemplate, @Qualifier("subBreedsRestTemplate") RestTemplate restTemplateSub) {
        this.restTemplate = restTemplate;
        this.restTemplateSub = restTemplateSub;
    }

    public ResponseEntity<String> getAllBreeds() {
        String url = baseUrl + "/api/breeds/list/all";
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> getBySubBreed(String subBreed) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/breed/{subBreed}/list")
                .buildAndExpand(subBreed)
                .toUriString();
        return restTemplateSub.getForEntity(url, String.class);
    }

    public ResponseEntity<String> getBySubBreedAndFrom(String subBreed, String from) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/breed/{subBreed}/{from}/images")
                .buildAndExpand(subBreed, from)
                .toUriString();
        return restTemplateSub.getForEntity(url, String.class);
    }

}