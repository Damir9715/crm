package com.example.crm2.dto;

public class JwtAuthenticationResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String word;

    public JwtAuthenticationResponse(String accessToken, String word) {
        this.accessToken = accessToken;
        this.word = word;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
