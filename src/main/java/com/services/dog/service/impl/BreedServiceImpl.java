package com.services.dog.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.services.dog.config.exception.CustomException;
import com.services.dog.config.httpclient.RestClient;
import com.services.dog.service.BreedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BreedServiceImpl implements BreedService {
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    @Override
    public Object list() throws JsonProcessingException {
        try {
            log.info("=========================================================>");
            log.info("Fetching breed list...");
            ResponseEntity<String> rsp = restClient.getAllBreeds();
            if (!rsp.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to fetch breed list: {} with status code: {}", rsp.getBody(), rsp.getStatusCode());
                throw new CustomException("Failed to fetch breed list", (HttpStatus) rsp.getStatusCode());
            }
            String responseBody = rsp.getBody();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            log.info("Breed list fetched successfully.");
            log.info("=========================================================>");
            return rootNode.get("message");

        } catch (JsonProcessingException e) {
            log.error("Error processing breed list response: {}", e.getMessage());
            throw new CustomException("Error processing breed list response", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object getBySubBreed(String subBreed) {
        try {
            log.info("=========================================================>");
            log.info("Fetching images for sub breed '{}'", subBreed);
            ResponseEntity<String> rsp = restClient.getBySubBreed(subBreed);
            if (!rsp.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to fetch images for sub breed {}: {} with code: {}", subBreed, rsp.getBody(),
                        rsp.getStatusCode());
                throw new CustomException("Failed to fetch images for sub breed "+ subBreed,
                        (HttpStatus) rsp.getStatusCode());
            }

            String responseBody = rsp.getBody();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode arrayNode = rootNode.get("message");
            log.info("Response fetching images for sub breed : {}", arrayNode);

            switch (subBreed) {
                case "shiba" -> {
                    log.info("Extracting odd images for sub breed shiba");
                    return extractOddImages(arrayNode);
                }
                case "sheepdog" -> {
                    log.info("Creating images object for sub breed sheepdog");
                    return createSheepdogImagesObject(subBreed, arrayNode);
                }
                case "terrier" -> {
                    log.info("Fetching images for sub breed terrier");
                    return fetchTerrierImages(subBreed, arrayNode);
                }
                default -> {
                    log.info("Returning images for sub breed '{}'", subBreed);
                    return arrayNode;
                }
            }
        } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
            log.error("Error fetching images for sub breed {}: {}", subBreed, e.getMessage());
            throw new CustomException("Error fetching breed for sub breed " + subBreed,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> extractOddImages(JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        if (arrayNode.isArray()) {
            for (int i = 0; i < arrayNode.size(); i++) {
                if (i % 2 != 0) {
                    JsonNode imageNode = arrayNode.get(i);
                    if (imageNode.isTextual()) {
                        result.add(imageNode.asText());
                    }
                }
            }
        }
        return result;
    }

    private ObjectNode createSheepdogImagesObject(String subBreed, JsonNode arrayNode) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        if (arrayNode.isArray()) {
            for (JsonNode subBreedNode : arrayNode) {
                if (subBreedNode.isTextual()) {
                    objectNode.putArray(subBreed + "-" + subBreedNode.asText());
                }
            }
        }
        return objectNode;
    }

    private Map<String, List<String>> fetchTerrierImages(String subBreed, JsonNode arrayNode)
            throws InterruptedException, ExecutionException {

        Map<String, List<String>> terrierImages = new HashMap<>();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (JsonNode sub : arrayNode) {
            log.info("Fetching images for sub breed {}", sub.asText());
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    ResponseEntity<String> rsp = restClient.getBySubBreedAndFrom(subBreed, sub.asText());
                    if (!rsp.getStatusCode().is2xxSuccessful()) {
                        log.error("Failed to fetch images for sub breed {}: {} with code: {}", sub.asText(),
                                rsp.getBody(), rsp.getStatusCode());
                        throw new CustomException("Failed to fetch images for sub breed " + sub.asText(),
                                (HttpStatus) rsp.getStatusCode());
                    }
                    String responseBody = rsp.getBody();
                    JsonNode rootNode = objectMapper.readTree(responseBody);

                    List<String> images = extractImages(rootNode.get("message"));
                    terrierImages.put(sub.asText(), images);
                    log.info("Added images for sub breed {}", sub.asText());
                } catch (JsonProcessingException e) {
                    log.error("Error processing response for sub breed {}: {}", sub.asText(), e.getMessage());
                    throw new CustomException("Error processing response for sub breed " + sub.asText(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            });
            futures.add(future);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get();
        log.info("fetching images for sub breed successfully");
        log.info("=========================================================>");
        return terrierImages;
    }

    private List<String> extractImages(JsonNode node) {
        List<String> images = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode imageNode : node) {
                if (imageNode.isTextual()) {
                    images.add(imageNode.asText());
                }
            }
        }
        return images;
    }
}
