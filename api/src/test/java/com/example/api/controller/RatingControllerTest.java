package com.example.api.controller;

import com.example.api.config.TestSecurityConfig;
import com.example.api.handler.GlobalExceptionHandler;
import com.example.dtos.RatingDto;
import com.example.services.RatingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    /** Simule un principal Long tel que le JwtAuthFilter le produit en production. */
    private Authentication authAs(Long userId) {
        return new UsernamePasswordAuthenticationToken(userId, null, List.of());
    }

    // ── GET /api/v1/recette/{id}/rating ───────────────────────────────────────

    @Test
    void getRating_anonyme_retourneOkSansNotePersonnelle() throws Exception {
        when(ratingService.getRating(1, null)).thenReturn(new RatingDto(4.2, 7L, null));

        mockMvc.perform(get("/api/v1/recette/1/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRate").value(4.2))
                .andExpect(jsonPath("$.ratingCount").value(7))
                .andExpect(jsonPath("$.userRating").isEmpty());
    }

    @Test
    void getRating_utilisateurConnecte_inclutSaNotePersonnelle() throws Exception {
        when(ratingService.getRating(1, 42L)).thenReturn(new RatingDto(3.5, 4L, 3));

        mockMvc.perform(get("/api/v1/recette/1/rating").with(authentication(authAs(42L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRating").value(3));
    }

    // ── POST /api/v1/recette/{id}/rating ──────────────────────────────────────

    @Test
    void submitRating_noteValide_retourneOkAvecDtoMisAJour() throws Exception {
        when(ratingService.submitRating(1, 42L, 5)).thenReturn(new RatingDto(4.8, 12L, 5));

        mockMvc.perform(post("/api/v1/recette/1/rating")
                        .with(authentication(authAs(42L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRate").value(4.8))
                .andExpect(jsonPath("$.userRating").value(5));
    }

    @Test
    void submitRating_nonAuthentifie_retourne401() throws Exception {
        // Le contrôleur retourne 401 si userId est null (utilisateur anonyme)
        mockMvc.perform(post("/api/v1/recette/1/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 5}"))
                .andExpect(status().isUnauthorized());

        verify(ratingService, never()).submitRating(any(), any(), anyInt());
    }

    @Test
    void submitRating_noteSuperieureA5_retourne400() throws Exception {
        mockMvc.perform(post("/api/v1/recette/1/rating")
                        .with(authentication(authAs(42L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 6}"))
                .andExpect(status().isBadRequest());

        verify(ratingService, never()).submitRating(any(), any(), anyInt());
    }

    @Test
    void submitRating_noteZero_retourne400() throws Exception {
        mockMvc.perform(post("/api/v1/recette/1/rating")
                        .with(authentication(authAs(42L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rating\": 0}"))
                .andExpect(status().isBadRequest());

        verify(ratingService, never()).submitRating(any(), any(), anyInt());
    }

    @Test
    void submitRating_corpsVide_retourne400() throws Exception {
        mockMvc.perform(post("/api/v1/recette/1/rating")
                        .with(authentication(authAs(42L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
