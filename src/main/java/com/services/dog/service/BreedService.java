package com.services.dog.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.concurrent.ExecutionException;

public interface BreedService {
    Object list() throws JsonProcessingException;
    Object getBySubBreed(String subBreed) throws JsonProcessingException, ExecutionException, InterruptedException;
}
