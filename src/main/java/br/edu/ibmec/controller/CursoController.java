package br.edu.ibmec.controller;

import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.CursoRepositoryService;
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
@RequestMapping("/api/curso")
@Tag(name = "Cursos")
public class CursoController {

    @Autowired
    private CursoRepositoryService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        return ResponseEntity.ok(cursoService.listarCursos());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<CursoDTO> buscarCurso(@PathVariable int codigo) throws DaoException {
        return ResponseEntity.ok(cursoService.buscarCurso(codigo));
    }

    @PostMapping
    public ResponseEntity<String> cadastrarCurso(@Valid @RequestBody CursoDTO cursoDTO) throws ServiceException {
        cursoService.cadastrarCurso(cursoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Curso cadastrado com sucesso");
    }

    @PutMapping
    public ResponseEntity<String> atualizarCurso(@Valid @RequestBody CursoDTO cursoDTO) throws ServiceException, DaoException {
        cursoService.alterarCurso(cursoDTO);
        return ResponseEntity.ok("Curso atualizado com sucesso");
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerCurso(@PathVariable int codigo) throws DaoException {
        cursoService.removerCurso(codigo);
        return ResponseEntity.ok("Curso removido com sucesso");
    }
}
