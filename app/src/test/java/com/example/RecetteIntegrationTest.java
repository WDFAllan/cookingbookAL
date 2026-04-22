package com.example;

import com.example.dtos.IngredientDto;
import com.example.dtos.RecetteDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration : contexte Spring complet + H2 en mémoire.
 * Chaque test est indépendant — il crée ses propres données avant d'agir.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RecetteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/recette";

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** Construit un DTO valide prêt à être envoyé. */
    private RecetteDto recetteValide(String nom) {
        RecetteDto dto = new RecetteDto();
        dto.setName(nom);
        dto.setRate(4.0);
        dto.setTags(List.of("dessert"));
        dto.setIngredients(List.of(new IngredientDto("Farine", 200, "g")));
        dto.setSteps(List.of("Mélanger", "Cuire 30 min"));
        return dto;
    }

    /**
     * Crée une recette via POST et retourne son ID.
     * Utilisé par les tests qui ont besoin d'une recette existante.
     */
    private int createRecipe(String nom) throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recetteValide(nom))))
                .andExpect(status().isCreated())
                .andReturn();

        RecetteDto created = objectMapper.readValue(
                result.getResponse().getContentAsString(), RecetteDto.class);
        return created.getId();
    }

    // ── Tests ────────────────────────────────────────────────────────────────

    /**
     * Création d'une recette valide.
     * Vérifie le statut 201, le nom retourné et la présence d'un ID.
     */
    @Test
    void createRecipe_valide_retourne201AvecLeDto() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recetteValide("Tarte aux pommes"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tarte aux pommes"))
                .andExpect(jsonPath("$.id").isNumber());
    }

    /**
     * Création avec un nom vide : la validation doit rejeter avec 400.
     * Le ProblemDetail retourné doit contenir un champ "detail".
     */
    @Test
    void createRecipe_nomVide_retourne400() throws Exception {
        RecetteDto dto = recetteValide("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    /**
     * Création sans ingrédients : la validation @NotEmpty doit rejeter.
     */
    @Test
    void createRecipe_sansIngredients_retourne400() throws Exception {
        RecetteDto dto = recetteValide("Recette sans ingrédients");
        dto.setIngredients(List.of());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").exists());
    }

    /**
     * Lecture de toutes les recettes.
     * Après avoir créé une recette, la liste ne doit pas être vide.
     */
    @Test
    void getAllRecettes_retourneListeNonVide() throws Exception {
        createRecipe("Quiche lorraine");

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    /**
     * Lecture d'une recette par son ID.
     * Vérifie que le nom correspond à ce qui a été créé.
     */
    @Test
    void getRecetteById_existante_retourneLeDto() throws Exception {
        int id = createRecipe("Soupe à l'oignon");

        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Soupe à l'oignon"))
                .andExpect(jsonPath("$.ingredients", hasSize(1)));
    }

    /**
     * Lecture d'un ID inexistant : le GlobalExceptionHandler doit retourner 404
     * avec un ProblemDetail contenant un message explicite.
     */
    @Test
    void getRecetteById_inexistante_retourne404() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value(containsString("999999")));
    }

    /**
     * Modification d'une recette existante.
     * Vérifie que le nouveau nom est bien persisté et retourné.
     */
    @Test
    void modifierRecette_valide_retourneLeDtoMisAJour() throws Exception {
        int id = createRecipe("Ancien nom");

        RecetteDto update = recetteValide("Nouveau nom");
        mockMvc.perform(put(BASE_URL + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nouveau nom"));
    }

    /**
     * Suppression d'une recette.
     * Vérifie le 204, puis que la recette est bien introuvable (404).
     */
    @Test
    void supprimerRecette_existante_retourne204PuisIntrouvable() throws Exception {
        int id = createRecipe("Recette à supprimer");

        // Suppression → 204
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        // Vérification : la recette n'existe plus → 404
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());
    }
}
