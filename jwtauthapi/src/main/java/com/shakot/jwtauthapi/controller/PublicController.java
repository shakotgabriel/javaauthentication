package com.shakot.jwtauthapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PublicController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "JWT Auth API is running");
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
