package services;

import com.example.dtos.LoginRequest;
import com.example.dtos.RegisterRequest;
import com.example.dtos.UserDto;
import com.example.exceptions.ForbiddenException;
import com.example.port.PasswordEncoderPort;
import com.example.port.UserPort;
import com.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserPort userPort;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private UserService userService;

    // ── Helpers ──────────────────────────────────────────────────────────────

    private RegisterRequest registerRequest(String username, String email, String password) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setEmail(email);
        req.setPassword(password);
        return req;
    }

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest req = new LoginRequest();
        req.setEmail(email);
        req.setPassword(password);
        return req;
    }

    private UserDto userDto(Long id, String username, String email) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setUsername(username);
        dto.setEmail(email);
        return dto;
    }

    // ── register ─────────────────────────────────────────────────────────────

    @Test
    void register_nouvelEmail_sauvegardeEtRetourneUserDto() {
        RegisterRequest req = registerRequest("alice", "alice@example.com", "password123");
        UserDto expected = userDto(1L, "alice", "alice@example.com");

        when(userPort.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userPort.save("alice", "alice@example.com", "hashed")).thenReturn(expected);

        UserDto result = userService.register(req);

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getUsername()).isEqualTo("alice");
        verify(userPort).save("alice", "alice@example.com", "hashed");
    }

    @Test
    void register_emailDejaUtilise_leveIllegalArgumentException() {
        RegisterRequest req = registerRequest("bob", "existing@example.com", "password123");
        when(userPort.existsByEmail("existing@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existing@example.com");

        verify(userPort, never()).save(any(), any(), any());
    }

    @Test
    void register_motDePasseHasheAvantSauvegarde() {
        RegisterRequest req = registerRequest("alice", "alice@example.com", "plaintext");
        when(userPort.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("plaintext")).thenReturn("$2a$hashed");
        when(userPort.save(any(), any(), eq("$2a$hashed"))).thenReturn(userDto(1L, "alice", "alice@example.com"));

        userService.register(req);

        verify(passwordEncoder).encode("plaintext");
        verify(userPort).save("alice", "alice@example.com", "$2a$hashed");
    }

    // ── authenticate ─────────────────────────────────────────────────────────

    @Test
    void authenticate_credentialsValides_retourneUserDto() {
        LoginRequest req = loginRequest("alice@example.com", "password123");
        UserDto expected = userDto(1L, "alice", "alice@example.com");

        when(userPort.findByEmail("alice@example.com")).thenReturn(Optional.of(expected));
        when(userPort.getHashedPassword("alice@example.com")).thenReturn("hashed");
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);

        UserDto result = userService.authenticate(req);

        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void authenticate_emailInconnu_leveNoSuchElementException() {
        LoginRequest req = loginRequest("unknown@example.com", "password123");
        when(userPort.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.authenticate(req))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void authenticate_mauvaisMotDePasse_leveForbiddenException() {
        LoginRequest req = loginRequest("alice@example.com", "wrongpassword");
        UserDto user = userDto(1L, "alice", "alice@example.com");

        when(userPort.findByEmail("alice@example.com")).thenReturn(Optional.of(user));
        when(userPort.getHashedPassword("alice@example.com")).thenReturn("hashed");
        when(passwordEncoder.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate(req))
                .isInstanceOf(ForbiddenException.class);
    }

    // ── updateUsername ────────────────────────────────────────────────────────

    @Test
    void updateUsername_delegueAuPortEtRetourneDtoMisAJour() {
        UserDto updated = userDto(1L, "nouveau_nom", "alice@example.com");
        when(userPort.updateUsername(1L, "nouveau_nom")).thenReturn(updated);

        UserDto result = userService.updateUsername(1L, "nouveau_nom");

        assertThat(result.getUsername()).isEqualTo("nouveau_nom");
        verify(userPort).updateUsername(1L, "nouveau_nom");
    }
}
