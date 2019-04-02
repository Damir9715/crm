package com.example.crm2.dto;

public class JwtAuthenticationResponse {

    private Integer id;
    private String accessToken;
    private String tokenType = "Bearer";
    private String role;
    private String username;

    public JwtAuthenticationResponse(Integer id, String accessToken, String role, String username) {
        this.id = id;
        this.accessToken = accessToken;
        this.role = role;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
