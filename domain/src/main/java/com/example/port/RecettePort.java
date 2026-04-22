package com.example.port;

import com.example.dtos.RecetteDto;

import java.util.List;
import java.util.Optional;

public interface RecettePort {

    List<RecetteDto> findAll();

    Optional<RecetteDto> findById(Integer id);

    List<RecetteDto> findAllByTags(List<String> tags);

    RecetteDto save(RecetteDto recetteDto);

    RecetteDto update(Integer id, RecetteDto recetteDto);

    void delete(Integer id);

    List<String> getAllTags();
}
