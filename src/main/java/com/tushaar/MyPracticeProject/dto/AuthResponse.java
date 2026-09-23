package com.tushaar.MyPracticeProject.dto;

//No user here just the token data
public class AuthResponse {
    private String token;

    public AuthResponse(String token) { this.token = token; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
