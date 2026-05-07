package com.example.api.controller;

import com.example.api.config.TestSecurityConfig;
import com.example.api.handler.GlobalExceptionHandler;
import com.example.dtos.UserDto;
import com.example.services.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /** Simule un utilisateur authentifié avec un userId Long, comme le JwtAuthFilter le produit. */
    private Authentication authAs(Long userId) {
        return new UsernamePasswordAuthenticationToken(userId, null, List.of());
    }

    // ── PATCH /api/v1/user/me ─────────────────────────────────────────────────

    @Test
    void updateUsername_valide_retourneOkAvecLeDto() throws Exception {
        UserDto updated = new UserDto();
        updated.setId(1L);
        updated.setUsername("nouveau_nom");
        updated.setEmail("alice@example.com");
        when(userService.updateUsername(1L, "nouveau_nom")).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/user/me")
                        .with(authentication(authAs(1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"nouveau_nom\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nouveau_nom"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void updateUsername_champUsernameAbsent_retourne400() throws Exception {
        // Le corps JSON ne contient pas la clé "username"
        mockMvc.perform(patch("/api/v1/user/me")
                        .with(authentication(authAs(1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"autreChamp\": \"valeur\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUsername(any(), any());
    }

    @Test
    void updateUsername_nomVide_retourne400() throws Exception {
        mockMvc.perform(patch("/api/v1/user/me")
                        .with(authentication(authAs(1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"\"}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUsername(any(), any());
    }

    @Test
    void updateUsername_nomBlanc_retourne400() throws Exception {
        mockMvc.perform(patch("/api/v1/user/me")
                        .with(authentication(authAs(1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"   \"}"))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUsername(any(), any());
    }

    @Test
    void updateUsername_nonAuthentifie_retourne401() throws Exception {
        // Aucun `.with(authentication(...))` → utilisateur anonyme
        mockMvc.perform(patch("/api/v1/user/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"hack\"}"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).updateUsername(any(), any());
    }

    @Test
    void updateUsername_nomAvecEspaces_esteTriméAvantAppelService() throws Exception {
        UserDto updated = new UserDto();
        updated.setUsername("nom_propre");
        when(userService.updateUsername(1L, "nom_propre")).thenReturn(updated);

        // Le contrôleur applique .trim() avant d'appeler le service
        mockMvc.perform(patch("/api/v1/user/me")
                        .with(authentication(authAs(1L)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"  nom_propre  \"}"))
                .andExpect(status().isOk());

        verify(userService).updateUsername(1L, "nom_propre");
    }
}
