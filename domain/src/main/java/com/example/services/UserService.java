package com.example.services;

import com.example.dtos.LoginRequest;
import com.example.dtos.RegisterRequest;
import com.example.dtos.UserDto;
import com.example.exceptions.ForbiddenException;
import com.example.port.PasswordEncoderPort;
import com.example.port.UserPort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserPort userPort;
    private final PasswordEncoderPort passwordEncoder;

    public UserService(UserPort userPort, PasswordEncoderPort passwordEncoder) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto register(RegisterRequest request) {
        if (userPort.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé : " + request.getEmail());
        }
        String hashed = passwordEncoder.encode(request.getPassword());
        return userPort.save(request.getUsername(), request.getEmail(), hashed);
    }

    public UserDto updateUsername(Long userId, String username) {
        return userPort.updateUsername(userId, username);
    }

    public UserDto authenticate(LoginRequest request) {
        UserDto user = userPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Aucun compte trouvé avec cet email"));
        String storedHash = userPort.getHashedPassword(request.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), storedHash)) {
            throw new ForbiddenException("Mot de passe incorrect");
        }
        return user;
    }
}
