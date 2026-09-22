package com.tushaar.MyPracticeProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        // strength 10 is the default; higher = slower = more secure
        return new BCryptPasswordEncoder();
    }
}
