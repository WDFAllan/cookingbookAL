package com.example.services;

import com.example.dtos.RecetteDto;
import com.example.port.RecettePort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RecetteService {

    private final RecettePort recettePort;

    public RecetteService(RecettePort recettePort) { this.recettePort = recettePort; }

    public List<RecetteDto> getAllRecette() {
       return recettePort.findAll();
    }

    public RecetteDto getRecetteById(Integer id) {
        return recettePort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recette introuvable : " + id));
    }

    public List<RecetteDto> getRecetteByTag(List<String> tags) {
        return recettePort.findAllByTags(tags);
    }

    public RecetteDto addRecette(RecetteDto recetteDto) {
        recetteDto.setDate(LocalDate.now());
        return recettePort.save(recetteDto);
    }

    public RecetteDto updateRecette(Integer id, RecetteDto recetteDto) {
        return recettePort.update(id, recetteDto);
    }

    public void deleteRecette(Integer id) {
        recettePort.delete(id);
    }

    public List<String> getAllTags(){
        return recettePort.getAllTags().stream().sorted().toList();
    }


}
