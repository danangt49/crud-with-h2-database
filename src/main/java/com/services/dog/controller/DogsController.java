package com.services.dog.controller;

import com.services.dog.config.GlobalApiResponse;
import com.services.dog.service.DogsService;
import com.services.dog.vo.DogsQueryVo;
import com.services.dog.vo.DogsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/dogs", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Dogs")
public class DogsController {
    private final DogsService dogsService;

    @Operation(summary = "Create new dog")
    @PostMapping
    public GlobalApiResponse<?> create(@RequestBody @Valid DogsVo vo) {
        return new GlobalApiResponse<>(dogsService.create(vo), HttpStatus.CREATED);
    }

    @Operation(summary = "Get dog with pagination")
    @GetMapping
    public GlobalApiResponse<?> page(DogsQueryVo vo, Pageable pageable) {
        return new GlobalApiResponse<>(dogsService.page(vo, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get list dog")
    @GetMapping("list")
    public GlobalApiResponse<?> list() {
        return new GlobalApiResponse<>(dogsService.list(), HttpStatus.OK);
    }

    @Operation(summary = "Get dog by id")
    @GetMapping("{id}")
    public GlobalApiResponse<?> getById(@PathVariable Long id) {
        return new GlobalApiResponse<>(dogsService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Update dog by id")
    @PutMapping("{id}")
    public GlobalApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid DogsVo vo) {
        return new GlobalApiResponse<>(dogsService.update(id, vo), HttpStatus.OK);
    }

    @Operation(summary = "Delete dog by id")
    @DeleteMapping("{id}")
    public GlobalApiResponse<?> delete(@PathVariable Long id) {
        return new GlobalApiResponse<>(dogsService.delete(id), HttpStatus.OK);
    }
}

