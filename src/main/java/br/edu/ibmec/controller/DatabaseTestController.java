package br.edu.ibmec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Controller para testes diretos com MySQL (perfil dev). */
@RestController
@Profile("dev")
@RequestMapping("/api/database")
@Tag(name = "Database Test", description = "Testes diretos no banco MySQL")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @Operation(summary = "Testar Conexão", description = "Testa conexão direta com MySQL")
    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> testConnection() {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = dataSource.getConnection()) {
            response.put("status", "Conexão MySQL OK! 🗄️");
            response.put("url", conn.getMetaData().getURL());
            response.put("driver", conn.getMetaData().getDriverName());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Erro na conexão");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Criar Tabela Teste", description = "Cria uma tabela de teste no MySQL")
    @PostMapping("/create-test-table")
    public ResponseEntity<Map<String, Object>> createTestTable() {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Criar tabela de teste
            String sql = """
                CREATE TABLE IF NOT EXISTS test_cursos (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    codigo INT NOT NULL,
                    nome VARCHAR(100) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.execute(sql);
            
            response.put("status", "Tabela test_cursos criada! ✅");
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Erro ao criar tabela");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Inserir Dados Teste", description = "Insere dados de teste na tabela")
    @PostMapping("/insert-test-data")
    public ResponseEntity<Map<String, Object>> insertTestData() {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO test_cursos (codigo, nome) VALUES (?, ?)")) {
            
            stmt.setInt(1, 1);
            stmt.setString(2, "Ciência da Computação");
            int rows = stmt.executeUpdate();
            
            response.put("status", "Dados inseridos! ✅");
            response.put("rows_affected", rows);
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Erro ao inserir dados");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Listar Dados", description = "Lista todos os dados da tabela de teste")
    @GetMapping("/list-test-data")
    public ResponseEntity<Map<String, Object>> listTestData() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> cursos = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM test_cursos")) {
            
            while (rs.next()) {
                Map<String, Object> curso = new HashMap<>();
                curso.put("id", rs.getInt("id"));
                curso.put("codigo", rs.getInt("codigo"));
                curso.put("nome", rs.getString("nome"));
                curso.put("created_at", rs.getTimestamp("created_at"));
                cursos.add(curso);
            }
            
            response.put("status", "Dados listados! 📋");
            response.put("count", cursos.size());
            response.put("cursos", cursos);
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "Erro ao listar dados");
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}