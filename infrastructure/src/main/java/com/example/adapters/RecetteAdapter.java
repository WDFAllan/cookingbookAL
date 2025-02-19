package com.example.adapters;

import com.example.dtos.RecetteDto;
import com.example.mapper.RecetteMapper;
import com.example.model.Recette;
import com.example.port.RecettePort;
import com.example.repository.RecetteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecetteAdapter implements RecettePort {

    private final RecetteRepository repo;
    private final RecetteMapper mapper;

    public RecetteAdapter(RecetteRepository repo, RecetteMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public List<RecetteDto> findAll() {
       List<Recette> list = repo.findAll();

        return mapper.map(list);
    }

    @Override
    public List<RecetteDto> findAllByTags(List<String> tags) {
        List<Recette> list = repo.findAllByTags(tags, (long) tags.size());

        return mapper.map(list);

    }

    @Override
    public RecetteDto save(RecetteDto recetteDto) {
        Recette recetteTocreate = mapper.map(recetteDto);
        Recette recette = repo.save(recetteTocreate);

        return mapper.map(recette);
    }

    @Override
    public List<String> getAllTags() {
        return repo.getAllTags();
    }


}
