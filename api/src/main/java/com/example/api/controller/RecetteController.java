package com.example.api.controller;

import com.example.dtos.RecetteDto;
import com.example.services.RecetteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/v1/recette")
public class RecetteController {

    private final RecetteService recetteService;


    public RecetteController(RecetteService recetteService) {this.recetteService = recetteService;}

    @GetMapping
    public List<RecetteDto> getAllRecettes() {
        return recetteService.getAllRecette();
    }

    @PostMapping
    public void createRecette(@RequestBody RecetteDto recetteDto) {
        recetteService.addRecette(recetteDto);
    }


}
