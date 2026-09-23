package com.tushaar.MyPracticeProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter  jwtFilter ;

    //Inject the JwtFilter in the constructor
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // strength 10 is the default; higher = slower = more secure
        return new BCryptPasswordEncoder();
    }

    @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

         http
                 //Disable CSRF
                 .csrf(csrf -> csrf.disable())

                 //Which url needs authentication
                 .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/auth/**").permitAll() //login/register upon localhost.com
                         .anyRequest().authenticated() //Everything else required token else it will not pass
                 )


                 //Tell spring not to create session
                 .sessionManagement(sess ->
                         sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                 //Put our custom filter in front of Spring's default filters
                 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // put it in front of UsernamePasswordAuthenticationFilter as we are verifying token before logging in

        //We are done
        return http.build();
    }
}
