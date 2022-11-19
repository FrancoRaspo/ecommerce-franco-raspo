package com.ecommerce.francoraspo.models.responses;

import java.util.List;

public class JwtResponse {
    final private String token;
    final private String userId;
    final private String username;
    final private String email;
    final private List<String> roles;

    public JwtResponse(String token, String userId, String username, String email, List<String> roles) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}
