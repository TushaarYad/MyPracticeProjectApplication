package com.tushaar.MyPracticeProject.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthRequest {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
