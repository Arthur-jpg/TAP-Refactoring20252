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
    public ResponseEntity<ProfessorDTO> buscarProfessor(@PathVariable Long id) throws DaoException {
        return ResponseEntity.ok(professorService.buscarProfessor(id));
    }

    @PostMapping
    public ResponseEntity<?> cadastrarProfessor(@Valid @RequestBody ProfessorDTO professorDTO) throws ServiceException {
        ProfessorDTO salvo = professorService.cadastrarProfessor(professorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping
    public ResponseEntity<String> atualizarProfessor(@Valid @RequestBody ProfessorDTO professorDTO) throws ServiceException, DaoException {
        professorService.alterarProfessor(professorDTO);
        return ResponseEntity.ok("Professor atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerProfessor(@PathVariable Long id) throws DaoException {
        professorService.removerProfessor(id);
        return ResponseEntity.ok("Professor removido com sucesso");
    }
}
