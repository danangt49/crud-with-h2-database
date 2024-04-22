package com.services.dog.service.impl;

import com.services.dog.config.exception.CustomException;
import com.services.dog.dto.DogsDto;
import com.services.dog.entity.Dogs;
import com.services.dog.repository.DogsRepository;
import com.services.dog.service.DogsService;
import com.services.dog.vo.DogsQueryVo;
import com.services.dog.vo.DogsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DogsServiceImpl implements DogsService {

    private final DogsRepository dogsRepository;
    Map<String, Object> response = new HashMap<>();

    private DogsDto toDto(Dogs original) {
        var bean = new DogsDto();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    @Override
    public Map<String, Object> create(DogsVo vo) {
        if (vo.getBreed().isEmpty() || vo.getImageUrls().isEmpty())
            throw new CustomException("Breed and Image URLs cannot be empty", HttpStatus.BAD_REQUEST);

        Dogs dogs = new Dogs();
        dogs.setBreed(vo.getBreed());
        dogs.setImageUrls(vo.getImageUrls());

        dogsRepository.save(dogs);
        response.put("message", "Success Add Data");
        return response;
    }

    @Override
    public List<DogsDto> list() {
        return dogsRepository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public Page<DogsDto> page(DogsQueryVo vo, Pageable pageable) {
        return dogsRepository.findByQuery(vo.getQ(), pageable).map(this::toDto);
    }

    @Override
    public DogsDto getById(Long id) {
        return toDto(dogsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Data not found" , HttpStatus.NOT_FOUND)));
    }

    @Override
    public Map<String, Object> update(Long id, DogsVo vo) {
        if (vo.getBreed().isEmpty() || vo.getImageUrls().isEmpty())
            throw new CustomException("Breed and Image URLs cannot be empty", HttpStatus.BAD_REQUEST);

        var dogs = dogsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Data not found" , HttpStatus.NOT_FOUND));

        dogs.setBreed(vo.getBreed());
        dogs.setImageUrls(vo.getImageUrls());

        dogsRepository.save(dogs);
        response.put("message", "Success Update Data");
        return response;
    }

    @Override
    public Map<String, Object> delete(Long id) {
        var dogs = dogsRepository.findById(id)
                .orElseThrow(() -> new CustomException("Data not found" , HttpStatus.NOT_FOUND));

        dogsRepository.delete(dogs);
        response.put("message", "Success Delete Data");
        return response;
    }
}
