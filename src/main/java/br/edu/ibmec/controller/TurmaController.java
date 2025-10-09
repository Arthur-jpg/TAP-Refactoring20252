package br.edu.ibmec.controller;

import java.util.ArrayList;
import java.util.Iterator;
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

import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.TurmaRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para gerenciamento de turmas.
 * Migrado de JAX-RS para Spring MVC seguindo Clean Code.
 */
@RestController
@RequestMapping("/api/turma")
@Tag(name = "Turmas", description = "API para gerenciamento de turmas do sistema universitário")
public class TurmaController {

    @Autowired
    private TurmaRepositoryService turmaRepositoryService;

    /**
     * Busca turma por código, ano e semestre.
     * GET /api/turma/{codigo}/{ano}/{semestre}
     */
    @Operation(summary = "Buscar turma", description = "Busca turma por código, ano e semestre")
    @GetMapping("/{codigo}/{ano}/{semestre}")
    public ResponseEntity<TurmaDTO> buscarTurma(
            @Parameter(description = "Código da turma") @PathVariable int codigo,
            @Parameter(description = "Ano da turma") @PathVariable int ano, 
            @Parameter(description = "Semestre (1 ou 2)") @PathVariable int semestre) {
        try {
            TurmaDTO turmaDTO = turmaRepositoryService.buscarTurma(codigo, ano, semestre);
            return ResponseEntity.ok(turmaDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cadastra nova turma.
     * POST /api/turma
     */
    @PostMapping
    public ResponseEntity<String> cadastrarTurma(@RequestBody TurmaDTO turmaDTO) {
        try {
            turmaRepositoryService.cadastrarTurma(turmaDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Turma cadastrada com sucesso");
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Código inválido");
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Ano inválido");
            }
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .body("Erro no banco de dados");
        }
    }

    /**
     * Atualiza turma existente.
     * PUT /api/turma
     */
    @PutMapping
    public ResponseEntity<String> alterarTurma(@RequestBody TurmaDTO turmaDTO) {
        try {
            turmaRepositoryService.alterarTurma(turmaDTO);
            return ResponseEntity.ok("Turma alterada com sucesso");
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Código inválido");
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Ano inválido");
            }
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .body("Erro no banco de dados");
        }
    }

    /**
     * Remove turma por código, ano e semestre.
     * DELETE /api/turma/{codigo}/{ano}/{semestre}
     */
    @DeleteMapping("/{codigo}/{ano}/{semestre}")
    public ResponseEntity<String> removerTurma(@PathVariable int codigo,
                                             @PathVariable int ano, 
                                             @PathVariable int semestre) {
        try {
            turmaRepositoryService.removerTurma(codigo, ano, semestre);
            return ResponseEntity.ok("Turma removida com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todas as turmas.
     * GET /api/turma
     */
    @GetMapping
    public ResponseEntity<List<TurmaDTO>> listarTurmas() {
        try {
            List<TurmaDTO> turmas = turmaRepositoryService.listarTurmasCompletas();
            return ResponseEntity.ok(turmas);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista turmas por disciplina.
     * GET /api/turma/disciplina/{codigoDisciplina}
     */
    @GetMapping("/disciplina/{codigoDisciplina}")
    public ResponseEntity<List<TurmaDTO>> listarTurmasPorDisciplina(@PathVariable int codigoDisciplina) {
        try {
            List<TurmaDTO> turmas = turmaRepositoryService.listarTurmasPorDisciplina(codigoDisciplina);
            return ResponseEntity.ok(turmas);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}