package br.edu.ibmec.controller;

import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.DisciplinaRepositoryService;
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
@RequestMapping("/api/disciplina")
@Tag(name = "Disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepositoryService disciplinaService;

    @GetMapping
    public ResponseEntity<List<DisciplinaDTO>> listarDisciplinas() {
        return ResponseEntity.ok(disciplinaService.listarDisciplinas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<DisciplinaDTO> buscarDisciplina(@PathVariable int codigo) {
        try {
            return ResponseEntity.ok(disciplinaService.buscarDisciplina(codigo));
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> cadastrarDisciplina(@Valid @RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            disciplinaService.cadastrarDisciplina(disciplinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Disciplina cadastrada com sucesso");
        } catch (ServiceException | DaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizarDisciplina(@Valid @RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            disciplinaService.alterarDisciplina(disciplinaDTO);
            return ResponseEntity.ok("Disciplina atualizada com sucesso");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerDisciplina(@PathVariable int codigo) {
        try {
            disciplinaService.removerDisciplina(codigo);
            return ResponseEntity.ok("Disciplina removida com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
