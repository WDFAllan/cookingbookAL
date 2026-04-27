package com.example.api.controller;

import com.example.dtos.IngredientDto;
import com.example.dtos.RecetteDto;
import com.example.services.IngredientService;
import com.example.services.RecetteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.example.api.handler.GlobalExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecetteController.class)
@Import(GlobalExceptionHandler.class)
class RecetteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecetteService recetteService;

    @MockitoBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    private RecetteDto recetteValide() {
        RecetteDto dto = new RecetteDto();
        dto.setName("Tarte aux pommes");
        dto.setRate(4.5);
        dto.setTags(List.of("dessert"));
        dto.setIngredients(List.of(new IngredientDto("Farine", 200, "g")));
        dto.setSteps(List.of("Mélanger les ingrédients", "Cuire 30 min"));
        return dto;
    }

    @Test
    void getAllRecettes_returnsOkWithList() throws Exception {
        when(recetteService.getAllRecette()).thenReturn(List.of(recetteValide()));

        mockMvc.perform(get("/api/v1/recette"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Tarte aux pommes"));
    }

    @Test
    void getRecetteById_existingId_returnsOk() throws Exception {
        when(recetteService.getRecetteById(1)).thenReturn(recetteValide());

        mockMvc.perform(get("/api/v1/recette/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tarte aux pommes"));
    }

    @Test
    void getRecetteById_unknownId_returns404() throws Exception {
        when(recetteService.getRecetteById(99))
                .thenThrow(new NoSuchElementException("Recette introuvable : 99"));

        mockMvc.perform(get("/api/v1/recette/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Recette introuvable : 99"));
    }

    @Test
    void createRecette_validBody_returnsCreated() throws Exception {
        RecetteDto dto = recetteValide();
        when(recetteService.addRecette(any(), any())).thenReturn(dto);

        mockMvc.perform(post("/api/v1/recette")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tarte aux pommes"));
    }

    @Test
    void createRecette_missingName_returns400() throws Exception {
        RecetteDto dto = new RecetteDto();
        dto.setIngredients(List.of(new IngredientDto("Farine", 200, "g")));
        dto.setSteps(List.of("Mélanger"));

        mockMvc.perform(post("/api/v1/recette")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    void updateRecette_validBody_returnsOk() throws Exception {
        RecetteDto dto = recetteValide();
        when(recetteService.updateRecette(eq(1), any(), any())).thenReturn(dto);

        mockMvc.perform(put("/api/v1/recette/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tarte aux pommes"));
    }

    @Test
    void deleteRecette_returnsNoContent() throws Exception {
        doNothing().when(recetteService).deleteRecette(eq(1), any());

        mockMvc.perform(delete("/api/v1/recette/1"))
                .andExpect(status().isNoContent());
    }
}
