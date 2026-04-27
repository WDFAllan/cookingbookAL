package com.example.adapters;

import com.example.dtos.UserDto;
import com.example.mapper.UserMapper;
import com.example.model.UserEntity;
import com.example.port.UserPort;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository repo;
    private final UserMapper mapper;

    public UserAdapter(UserRepository repo, UserMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public UserDto save(String username, String email, String hashedPassword) {
        UserEntity entity = new UserEntity();
        entity.setUsername(username);
        entity.setEmail(email);
        entity.setPassword(hashedPassword);
        entity.setCreatedAt(LocalDateTime.now());
        return mapper.map(repo.save(entity));
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return repo.findByEmail(email).map(mapper::map);
    }

    @Override
    public String getHashedPassword(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable : " + email))
                .getPassword();
    }

    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public UserDto updateUsername(Long userId, String username) {
        UserEntity entity = repo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable : " + userId));
        entity.setUsername(username);
        return mapper.map(repo.save(entity));
    }
}
