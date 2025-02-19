package com.example.api.controller;

import com.example.dtos.RecetteDto;
import com.example.services.RecetteService;
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

    @GetMapping(path = "/getByTags")
    public List<RecetteDto> getRecetteByTag(@RequestParam List<String> tags) {return recetteService.getRecetteByTag(tags);}

    @PostMapping
    public void createRecette(@RequestBody RecetteDto recetteDto) {
        recetteService.addRecette(recetteDto);
    }

    @GetMapping(path = "/getAllTags")
    public List<String> getAllTags() {
        return recetteService.getAllTags();
    }


}
