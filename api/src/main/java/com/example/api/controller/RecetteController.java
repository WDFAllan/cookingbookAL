package com.example.api.controller;

import com.example.dtos.RecetteDto;
import com.example.services.RecetteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/api/v1/recette")
public class RecetteController {

    private final RecetteService recetteService;

    public RecetteController(RecetteService recetteService) {this.recetteService = recetteService;}

    @GetMapping
    public List<RecetteDto> getAllRecettes() {
        return recetteService.getAllRecette();
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
        return ResponseEntity.status(HttpStatus.CREATED).body(recetteService.addRecette(recetteDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetteDto> updateRecette(@PathVariable Integer id, @Valid @RequestBody RecetteDto recetteDto) {
        return ResponseEntity.ok(recetteService.updateRecette(id, recetteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecette(@PathVariable Integer id) {
        recetteService.deleteRecette(id);
        return ResponseEntity.noContent().build();
    }

}
