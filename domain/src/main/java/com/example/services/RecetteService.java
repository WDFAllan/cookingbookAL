package com.example.services;

import com.example.dtos.RecetteDto;
import com.example.port.RecettePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecetteService {

    private final RecettePort recettePort;

    public RecetteService(RecettePort recettePort) { this.recettePort = recettePort; }

    public List<RecetteDto> getAllRecette() {
       return recettePort.findAll();
    }


    public void addRecette(RecetteDto recetteDto) {
        recettePort.save(recetteDto);
    }


}
