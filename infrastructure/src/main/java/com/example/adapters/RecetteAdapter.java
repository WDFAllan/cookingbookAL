package com.example.adapters;

import com.example.dtos.IngredientDto;
import com.example.dtos.RecetteDto;
import com.example.mapper.IngredientMapper;
import com.example.mapper.RecetteMapper;
import com.example.model.Recette;
import com.example.port.RecettePort;
import com.example.repository.RecetteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class RecetteAdapter implements RecettePort {

    private final RecetteRepository repo;
    private final RecetteMapper mapper;
    private final IngredientMapper ingredientMapper;

    public RecetteAdapter(RecetteRepository repo, RecetteMapper mapper, IngredientMapper ingredientMapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<RecetteDto> findAll() {
        return mapper.map(repo.findAll());
    }

    @Override
    public Optional<RecetteDto> findById(Integer id) {
        return repo.findById(id).map(mapper::map);
    }

    @Override
    public List<RecetteDto> findAllByTags(List<String> tags) {
        return mapper.map(repo.findAllByTags(tags, (long) tags.size()));
    }

    @Override
    public RecetteDto save(RecetteDto recetteDto) {
        Recette recette = repo.save(mapper.map(recetteDto));
        return mapper.map(recette);
    }

    @Override
    public RecetteDto update(Integer id, RecetteDto dto) {
        Recette existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recette introuvable : " + id));

        existing.setName(dto.getName());
        existing.setRate(dto.getRate());
        existing.setTags(dto.getTags());
        existing.setSteps(dto.getSteps());
        existing.setImageUrl(dto.getImageUrl());
        existing.setPrepTime(dto.getPrepTime());
        existing.setServings(dto.getServings());

        existing.getIngredients().clear();
        for (IngredientDto ingredientDto : dto.getIngredients()) {
            existing.getIngredients().add(ingredientMapper.map(ingredientDto));
        }

        return mapper.map(repo.save(existing));
    }

    @Override
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<String> getAllTags() {
        return repo.getAllTags();
    }


}
