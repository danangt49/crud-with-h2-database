package com.services.dog.service;

import com.services.dog.dto.DogsDto;
import com.services.dog.vo.DogsQueryVo;
import com.services.dog.vo.DogsVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface DogsService {
    Map<String, Object> create(DogsVo vo);
    List<DogsDto> list();
    Page<DogsDto> page(DogsQueryVo vo, Pageable pageable);
    DogsDto getById(Long id);
    Map<String, Object> update(Long id, DogsVo vo);
    Map<String, Object> delete(Long id);
}
