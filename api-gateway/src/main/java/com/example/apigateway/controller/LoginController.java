package com.example.apigateway.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Value("${JWT_SECRET}")
    private String secret;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> dados) {

        String username = dados.get("username");
        String password = dados.get("password");


        if (!"admin".equals(username) || !"123456".equals(password)) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        SecretKey key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}