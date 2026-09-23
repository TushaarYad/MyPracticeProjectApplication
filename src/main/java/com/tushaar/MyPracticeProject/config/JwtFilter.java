package com.tushaar.MyPracticeProject.config;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


/*
OncePerRequestFilter — a Spring Security base class that guarantees this filter runs exactly once per request.
If we implemented Filter directly, it could run multiple times in some scenarios.

request.getHeader("Authorization") — reads the HTTP header Authorization. Clients send the token like this:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

The Bearer prefix is a convention (defined by OAuth). It literally means "this request carries a bearer token."

SecurityContextHolder.getContext().setAuthentication(auth) — this is the crucial line. It tells Spring Security:
"for the duration of this request, the current user is username."
Spring stores this in a thread-local variable. Every downstream code (controllers, services) can then access it via SecurityContextHolder.getContext().getAuthentication().

chain.doFilter(request, response) — pass the request to the next filter in the chain. If you forget this line, the request never reaches your controller and the client hangs.

Why Collections.emptyList() for authorities? In a fuller setup,
you'd list roles (ROLE_ADMIN, ROLE_USER). We're skipping roles for now — everyone with a valid token is treated the same.

What happens if the token is invalid? The if block is skipped. No authentication is set. The request continues with no authenticated user.
Then Spring Security's later filters check: "is this endpoint allowed for anonymous users?" If not → 403 Forbidden.

*/
import java.util.ArrayList;
import java.util.Collections;

@Component
//Before reaching the controller, a bouncer thing checks the request
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    //Inject the token creator thing into dis
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException, java.io.IOException {
        //I need to look for the header from the
        String authHeader = request.getHeader("Authorization");

        //If it exists, extract the token starting with Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); //we need the first 7 characters to store in the token, for example: Bearer eyJhbG... --> eyJhbG...

            System.out.println("Token Received: " + token);

            if(jwtUtil.validateToken(token)) { //If it is valid using our validateToken I wrote
                String username = jwtUtil.extractUsername(token); // get username
                //Tell Spring Security "this request is authenticated as `username`" using the second method
                var auth = new UsernamePasswordAuthenticationToken(username, //who is this cuh
                        null, //Currently no password needed
                        Collections.emptyList() //Authorization roles, what can the user do will be stored in the collection
                );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Granted permission to " + username);

            }

        }
        chain.doFilter(request, response);

    }
}
