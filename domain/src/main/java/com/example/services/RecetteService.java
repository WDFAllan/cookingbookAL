package com.example.services;

import com.example.dtos.RecetteDto;
import com.example.port.RecettePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecetteService {

    private final RecettePort recettePort;

    public RecetteService(RecettePort recettePort) { this.recettePort = recettePort; }

    public List<RecetteDto> getAllRecette() {
       return recettePort.findAll();
    }

    public List<RecetteDto> getRecetteByTag(List<String> tags) {
        return recettePort.findAllByTags(tags);
    }


    public RecetteDto addRecette(RecetteDto recetteDto) {
        recetteDto.setDate(LocalDate.now());
        return recettePort.save(recetteDto);
    }


}
