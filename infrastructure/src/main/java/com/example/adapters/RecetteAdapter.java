package com.example.adapters;

import com.example.dtos.IngredientDto;
import com.example.dtos.PageResult;
import com.example.dtos.RecetteDto;
import com.example.exceptions.ForbiddenException;
import com.example.mapper.IngredientMapper;
import com.example.mapper.RecetteMapper;
import com.example.model.Recette;
import com.example.port.RecettePort;
import com.example.repository.RecetteRepository;
import com.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class RecetteAdapter implements RecettePort {

    private final RecetteRepository repo;
    private final RecetteMapper mapper;
    private final IngredientMapper ingredientMapper;
    private final UserRepository userRepository;

    public RecetteAdapter(RecetteRepository repo, RecetteMapper mapper, IngredientMapper ingredientMapper, UserRepository userRepository) {
        this.repo = repo;
        this.mapper = mapper;
        this.ingredientMapper = ingredientMapper;
        this.userRepository = userRepository;
    }

    private RecetteDto withAuthor(RecetteDto dto) {
        if (dto.getUserId() != null) {
            userRepository.findById(dto.getUserId()).ifPresent(user -> {
                String display = (user.getUsername() != null && !user.getUsername().isBlank())
                        ? user.getUsername()
                        : user.getEmail();
                dto.setAuthorEmail(display);
            });
        }
        return dto;
    }

    @Override
    public List<RecetteDto> findAll() {
        return mapper.map(repo.findAll()).stream().map(this::withAuthor).toList();
    }

    @Override
    public PageResult<RecetteDto> findAllPaged(int page, int size, String name, String sort) {
        Sort ordering = "rate".equals(sort)
                ? Sort.by("rate").descending()
                : Sort.by("date").descending();
        PageRequest pageable = PageRequest.of(page, size, ordering);
        Page<Recette> result = (name == null || name.isBlank())
                ? repo.findAll(pageable)
                : repo.findByNameContaining(name, pageable);
        List<RecetteDto> content = result.getContent().stream()
                .map(mapper::map)
                .map(this::withAuthor)
                .toList();
        return new PageResult<>(content, page, result.getTotalPages(), result.getTotalElements());
    }

    @Override
    public Optional<RecetteDto> findById(Integer id) {
        return repo.findById(id).map(mapper::map).map(this::withAuthor);
    }

    @Override
    public List<RecetteDto> findAllByTags(List<String> tags) {
        return mapper.map(repo.findAllByTags(tags, (long) tags.size())).stream().map(this::withAuthor).toList();
    }

    @Override
    public RecetteDto save(RecetteDto recetteDto, Long userId) {
        Recette recette = mapper.map(recetteDto);
        recette.setUserId(userId);
        return withAuthor(mapper.map(repo.save(recette)));
    }

    @Override
    public RecetteDto update(Integer id, RecetteDto dto, Long userId) {
        Recette existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recette introuvable : " + id));

        checkOwnership(existing.getUserId(), userId);

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

        return withAuthor(mapper.map(repo.save(existing)));
    }

    @Override
    public void delete(Integer id, Long userId) {
        Recette existing = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Recette introuvable : " + id));
        checkOwnership(existing.getUserId(), userId);
        repo.deleteById(id);
    }

    @Override
    public List<String> getAllTags() {
        return repo.getAllTags();
    }

    @Override
    public List<RecetteDto> findByUserId(Long userId) {
        return mapper.map(repo.findByUserId(userId)).stream().map(this::withAuthor).toList();
    }

    private void checkOwnership(Long ownerId, Long requesterId) {
        if (ownerId != null && !ownerId.equals(requesterId)) {
            throw new ForbiddenException("Vous n'êtes pas autorisé à modifier cette recette");
        }
    }
}
