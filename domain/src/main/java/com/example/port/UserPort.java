package com.example.port;

import com.example.dtos.UserDto;

import java.util.Optional;

public interface UserPort {
    UserDto save(String username, String email, String hashedPassword);
    Optional<UserDto> findByEmail(String email);
    String getHashedPassword(String email);
    boolean existsByEmail(String email);
    UserDto updateUsername(Long userId, String username);
}
