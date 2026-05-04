package com.example.services;

import com.example.dtos.PageResult;
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

    public PageResult<RecetteDto> getAllRecettePaged(int page, int size, String name, String sort) {
        return recettePort.findAllPaged(page, size, name, sort);
    }

    public RecetteDto getRecetteById(Integer id) {
        return recettePort.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recette introuvable : " + id));
    }

    public List<RecetteDto> getRecetteByTag(List<String> tags) {
        return recettePort.findAllByTags(tags);
    }

    public RecetteDto addRecette(RecetteDto recetteDto, Long userId) {
        recetteDto.setDate(LocalDate.now());
        return recettePort.save(recetteDto, userId);
    }

    public RecetteDto updateRecette(Integer id, RecetteDto recetteDto, Long userId) {
        return recettePort.update(id, recetteDto, userId);
    }

    public void deleteRecette(Integer id, Long userId) {
        recettePort.delete(id, userId);
    }

    public List<String> getAllTags() {
        return recettePort.getAllTags().stream().sorted().toList();
    }

    public List<RecetteDto> getRecettesByUserId(Long userId) {
        return recettePort.findByUserId(userId);
    }
}
