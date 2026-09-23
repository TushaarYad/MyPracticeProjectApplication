package com.tushaar.MyPracticeProject.controller;

import com.tushaar.MyPracticeProject.dto.AuthRequest;
import com.tushaar.MyPracticeProject.dto.AuthResponse;
import com.tushaar.MyPracticeProject.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    //Here we need just to inject the authService and pass on the value
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    //Create an DTO AuthResponse from a AuthRequest sent via JSON body containing username and password
    //@Valid to make sure both are provided else throw to global
    public AuthResponse login (@Valid @RequestBody AuthRequest authRequest) {
        return new AuthResponse(authService.login(authRequest));
    }
}
