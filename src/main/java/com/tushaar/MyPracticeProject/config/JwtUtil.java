package com.tushaar.MyPracticeProject.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

//Put in the IoC like a bean
@Component
public class JwtUtil {
    //This class has one job: create and verify JWTs. No Spring Security ahh ahh knowledge. Just pure JWT logic.
    // The secret key used to sign tokens. Stays here no one can get it.
    // Anyone with this key can forge tokens, so it MUST be secret in production.

    // For a practice project, we can use a hard coded value
    private final SecretKey key = Keys.hmacShaKeyFor("this-is-a-very-secret-key-change-it-in-prod-12345".getBytes());
    //Keys.hmacShaKeyFor takes a byte array (your secret string) and derives a proper SecretKey for the HMAC-SHA algorithm. The string must be at least 32 characters for HS256.

    //The life time on the token given in millisec
    private final long EXPIRATION_TIME = 1000 * 60 * 60; //For now, an hour

    //Creating the token with the key
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date (System.currentTimeMillis() + EXPIRATION_TIME)) //Given from when it was created, 1 hour from now
                .signWith(key)
                .compact(); // serialize into the "xxx.yyy.zzz" string
    }

    //Reads a token from a user
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload() //reads the claim, payload
                .getSubject(); //reads the sub, username
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        }
        catch (Exception e) {
            //Return fail if any of the requirements weren't met
            return false;
        }
    }

}
