package com.example.api.controller;

import com.example.dtos.IngredientDto;
import com.example.services.IngredientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.example.api.handler.GlobalExceptionHandler;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredientController.class)
@Import({GlobalExceptionHandler.class, com.example.api.config.TestSecurityConfig.class})
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IngredientService ingredientService;

    @Autowired
    private ObjectMapper objectMapper;

    // ── GET /api/v1/ingredient ────────────────────────────────────────────────

    @Test
    void getAllIngredients_retourneOkAvecLaListe() throws Exception {
        List<IngredientDto> ingredients = List.of(
                new IngredientDto("Farine", 200, "g"),
                new IngredientDto("Sucre", 100, "g")
        );
        when(ingredientService.getAllIngredients()).thenReturn(ingredients);

        mockMvc.perform(get("/api/v1/ingredient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Farine"))
                .andExpect(jsonPath("$[1].name").value("Sucre"));
    }

    @Test
    void getAllIngredients_listeVide_retourneOkAvecTableauVide() throws Exception {
        when(ingredientService.getAllIngredients()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/ingredient"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── POST /api/v1/ingredient ───────────────────────────────────────────────

    @Test
    void addIngredient_corpsValide_retourne200EtAppelleLeService() throws Exception {
        List<IngredientDto> toAdd = List.of(
                new IngredientDto("Beurre", 50, "g"),
                new IngredientDto("Sel", 5, "g")
        );
        doNothing().when(ingredientService).addIngredient(any());

        mockMvc.perform(post("/api/v1/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toAdd)))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).addIngredient(any());
    }

    @Test
    void addIngredient_listeVide_retourne200EtAppelleLeService() throws Exception {
        doNothing().when(ingredientService).addIngredient(any());

        mockMvc.perform(post("/api/v1/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).addIngredient(argThat(List::isEmpty));
    }

    @Test
    void addIngredient_unSeulIngredient_retourne200EtAppelleLeService() throws Exception {
        List<IngredientDto> toAdd = List.of(new IngredientDto("Vanille", 1, "gousse"));
        doNothing().when(ingredientService).addIngredient(any());

        mockMvc.perform(post("/api/v1/ingredient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toAdd)))
                .andExpect(status().isOk());

        verify(ingredientService, times(1)).addIngredient(any());
    }
}
