package com.healthconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "HEALTHCONNECT Backend API",
                "swaggerDocumentation", "http://localhost:8080/swagger-ui.html",
                "frontendUrl", "http://localhost:3000",
                "healthCheck", "http://localhost:8080/actuator/health"
        ));
    }
}
