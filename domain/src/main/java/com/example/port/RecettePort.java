package com.example.port;

import com.example.dtos.RecetteDto;

import java.util.List;

public interface RecettePort {

    List<RecetteDto> findAll();

    RecetteDto save(RecetteDto recetteDto);
}
