package com.example.port;

import com.example.dtos.RecetteDto;

import java.util.List;

public interface RecettePort {

    List<RecetteDto> findAll();

    List<RecetteDto> findAllByTags(List<String> tags);

    RecetteDto save(RecetteDto recetteDto);
}
