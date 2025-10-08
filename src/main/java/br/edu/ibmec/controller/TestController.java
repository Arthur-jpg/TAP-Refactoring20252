package br.edu.ibmec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller para testes básicos da API.
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "Teste", description = "Endpoints para testar se a API está funcionando")
public class TestController {

    @Operation(summary = "Health Check", description = "Verifica se a API está funcionando")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "API está funcionando! ✅");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Sistema Universitário - Spring Boot + MySQL + Swagger");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Database Check", description = "Testa a conexão com o banco de dados")
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> databaseCheck() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Simula um teste básico - na prática você faria uma query real
            response.put("database", "MySQL conectado! 🗄️");
            response.put("status", "OK");
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("database", "Erro na conexão");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}