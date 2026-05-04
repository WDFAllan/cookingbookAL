package com.example.api.controller;

import com.example.dtos.PageResult;
import com.example.dtos.RecetteDto;
import com.example.services.RecetteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/recette")
public class RecetteController {

    private final RecetteService recetteService;

    public RecetteController(RecetteService recetteService) { this.recetteService = recetteService; }

    @GetMapping
    public PageResult<RecetteDto> getAllRecettes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(defaultValue = "date") String sort) {
        return recetteService.getAllRecettePaged(page, size, name, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetteDto> getRecetteById(@PathVariable Integer id) {
        return ResponseEntity.ok(recetteService.getRecetteById(id));
    }

    @GetMapping(path = "/getByTags")
    public List<RecetteDto> getRecetteByTag(@RequestParam List<String> tags) {
        return recetteService.getRecetteByTag(tags);
    }

    @GetMapping(path = "/getAllTags")
    public List<String> getAllTags() {
        return recetteService.getAllTags();
    }

    @PostMapping
    public ResponseEntity<RecetteDto> createRecette(@Valid @RequestBody RecetteDto recetteDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recetteService.addRecette(recetteDto, getAuthenticatedUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetteDto> updateRecette(@PathVariable Integer id,
                                                    @Valid @RequestBody RecetteDto recetteDto) {
        return ResponseEntity.ok(recetteService.updateRecette(id, recetteDto, getAuthenticatedUserId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecette(@PathVariable Integer id) {
        recetteService.deleteRecette(id, getAuthenticatedUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public List<RecetteDto> getMyRecettes() {
        return recetteService.getRecettesByUserId(getAuthenticatedUserId());
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return (Long) auth.getPrincipal();
    }
}
