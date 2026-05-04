package com.example.port;

import com.example.dtos.PageResult;
import com.example.dtos.RecetteDto;

import java.util.List;
import java.util.Optional;

public interface RecettePort {

    List<RecetteDto> findAll();

    PageResult<RecetteDto> findAllPaged(int page, int size, String name, String sort);

    Optional<RecetteDto> findById(Integer id);

    List<RecetteDto> findAllByTags(List<String> tags);

    RecetteDto save(RecetteDto recetteDto, Long userId);

    RecetteDto update(Integer id, RecetteDto recetteDto, Long userId);

    void delete(Integer id, Long userId);

    List<String> getAllTags();

    List<RecetteDto> findByUserId(Long userId);
}
