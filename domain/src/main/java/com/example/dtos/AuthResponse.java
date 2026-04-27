package com.example.dtos;

public class AuthResponse {

    private String token;
    private String email;
    private Long userId;
    private String username;

    public AuthResponse() {}

    public AuthResponse(String token, String email, Long userId, String username) {
        this.token = token;
        this.email = email;
        this.userId = userId;
        this.username = username;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
