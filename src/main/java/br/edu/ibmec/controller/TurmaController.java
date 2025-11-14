package br.edu.ibmec.controller;

import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.TurmaRepositoryService;
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
@RequestMapping("/api/turma")
@Tag(name = "Turmas")
public class TurmaController {

    @Autowired
    private TurmaRepositoryService turmaService;

    @GetMapping
    public ResponseEntity<List<TurmaDTO>> listarTurmas() {
        return ResponseEntity.ok(turmaService.listarTurmas());
    }

    @GetMapping("/{codigo}/{ano}/{semestre}")
    public ResponseEntity<TurmaDTO> buscarTurma(@PathVariable int codigo,
                                                @PathVariable int ano,
                                                @PathVariable int semestre) throws DaoException {
        return ResponseEntity.ok(turmaService.buscarTurma(codigo, ano, semestre));
    }

    @PostMapping
    public ResponseEntity<String> cadastrarTurma(@Valid @RequestBody TurmaDTO turmaDTO) throws ServiceException, DaoException {
        turmaService.cadastrarTurma(turmaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Turma cadastrada com sucesso");
    }

    @PutMapping
    public ResponseEntity<String> atualizarTurma(@Valid @RequestBody TurmaDTO turmaDTO) throws ServiceException, DaoException {
        turmaService.alterarTurma(turmaDTO);
        return ResponseEntity.ok("Turma atualizada com sucesso");
    }

    @DeleteMapping("/{codigo}/{ano}/{semestre}")
    public ResponseEntity<String> removerTurma(@PathVariable int codigo,
                                               @PathVariable int ano,
                                               @PathVariable int semestre) throws DaoException {
        turmaService.removerTurma(codigo, ano, semestre);
        return ResponseEntity.ok("Turma removida com sucesso");
    }
}
