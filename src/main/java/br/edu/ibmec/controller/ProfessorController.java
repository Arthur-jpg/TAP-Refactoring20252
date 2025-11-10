package br.edu.ibmec.controller;

import br.edu.ibmec.dto.ProfessorDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.ProfessorRepositoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/professor")
@Tag(name = "Professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepositoryService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> listarProfessores() {
        return ResponseEntity.ok(professorService.listarProfessores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> buscarProfessor(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(professorService.buscarProfessor(id));
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProfessor(@Valid @RequestBody ProfessorDTO professorDTO) {
        try {
            ProfessorDTO salvo = professorService.cadastrarProfessor(professorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizarProfessor(@Valid @RequestBody ProfessorDTO professorDTO) {
        try {
            professorService.alterarProfessor(professorDTO);
            return ResponseEntity.ok("Professor atualizado com sucesso");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerProfessor(@PathVariable Long id) {
        try {
            professorService.removerProfessor(id);
            return ResponseEntity.ok("Professor removido com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
