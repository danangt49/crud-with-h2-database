package com.services.dog.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.services.dog.config.GlobalApiResponse;
import com.services.dog.service.BreedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/breeds", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "breed", description = "get data in third party")
public class BreedController {

    private final BreedService breedService;

    @Operation(summary = "Get list breed")
    @GetMapping("list")
    public GlobalApiResponse<?> list() throws JsonProcessingException {
        return new GlobalApiResponse<>(breedService.list(), HttpStatus.OK);
    }

    @Operation(summary = "Get breed by sub breed")
    @GetMapping("{subBreed}")
    public GlobalApiResponse<?> getBySubBreed(@PathVariable String subBreed)
            throws JsonProcessingException, ExecutionException, InterruptedException {
        return new GlobalApiResponse<>(breedService.getBySubBreed(subBreed), HttpStatus.OK);
    }
}
