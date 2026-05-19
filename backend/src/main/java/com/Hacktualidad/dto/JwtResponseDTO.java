package com.Hacktualidad.dto;

import com.Hacktualidad.Enums.Role;

public class JwtResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private Role role;

    public JwtResponseDTO(String accessToken, Long id, String email, Role role) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}