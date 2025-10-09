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

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.InscricaoService;
import br.edu.ibmec.service.InscricaoRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para gerenciamento de inscrições.
 * Migrado de JAX-RS para Spring MVC seguindo Clean Code.
 * 
 * @author Thiago Silva de Souza
 * @version 2.0 - Migrado para Spring Boot
 * @since 2012-02-29
 */
@RestController
@RequestMapping("/api/inscricao")
@Tag(name = "Inscrições", description = "API para gerenciamento de inscrições de alunos em turmas")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private InscricaoRepositoryService inscricaoRepositoryService;

    /**
     * Busca inscrição por matrícula do aluno e dados da turma.
     * GET /api/inscricao/{matricula}/{codigo}/{ano}/{semestre}
     */
    @Operation(summary = "Buscar inscrição", description = "Busca inscrição de aluno em turma específica")
    @GetMapping("/{matricula}/{codigo}/{ano}/{semestre}")
    public ResponseEntity<InscricaoDTO> buscarInscricao(
            @Parameter(description = "Matrícula do aluno") @PathVariable int matricula, 
            @Parameter(description = "Código da turma") @PathVariable int codigo,
            @Parameter(description = "Ano da turma") @PathVariable int ano, 
            @Parameter(description = "Semestre da turma") @PathVariable int semestre) {
        try {
            InscricaoDTO inscricaoDTO = inscricaoRepositoryService.buscarInscricao(matricula, codigo, ano, semestre);
            return ResponseEntity.ok(inscricaoDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cadastra nova inscrição.
     * POST /api/inscricao
     */
    @PostMapping
    public ResponseEntity<String> cadastrarInscricao(@RequestBody InscricaoDTO inscricaoDTO) {
        try {
            inscricaoRepositoryService.cadastrarInscricao(inscricaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Inscrição cadastrada com sucesso");
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
                    .body("Erro no banco de dados: " + e.getMessage());
        }
    }

    /**
     * Atualiza inscrição existente.
     * PUT /api/inscricao
     */
    @PutMapping
    public ResponseEntity<String> alterarInscricao(@RequestBody InscricaoDTO inscricaoDTO) {
        try {
            inscricaoRepositoryService.alterarInscricao(inscricaoDTO);
            return ResponseEntity.ok("Inscrição alterada com sucesso");
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
                    .body("Erro no banco de dados: " + e.getMessage());
        }
    }

    /**
     * Remove inscrição por matrícula e turma.
     * DELETE /api/inscricao/{matricula}/{codigo}/{ano}/{semestre}
     */
    @DeleteMapping("/{matricula}/{codigo}/{ano}/{semestre}")
    public ResponseEntity<String> removerInscricao(@PathVariable int matricula,
                                                 @PathVariable int codigo,
                                                 @PathVariable int ano, 
                                                 @PathVariable int semestre) {
        try {
            inscricaoRepositoryService.removerInscricao(matricula, codigo, ano, semestre);
            return ResponseEntity.ok("Inscrição removida com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todas as inscrições.
     * GET /api/inscricao
     */
    @GetMapping
    public ResponseEntity<List<InscricaoDTO>> listarInscricoes() {
        try {
            List<InscricaoDTO> inscricoes = inscricaoRepositoryService.listarInscricoesCompletas();
            return ResponseEntity.ok(inscricoes);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista inscrições por aluno.
     * GET /api/inscricao/aluno/{matricula}
     */
    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesPorAluno(@PathVariable int matricula) {
        try {
            List<InscricaoDTO> inscricoes = inscricaoRepositoryService.listarInscricoesPorAluno(matricula);
            return ResponseEntity.ok(inscricoes);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista inscrições por turma.
     * GET /api/inscricao/turma/{codigo}/{ano}/{semestre}
     */
    @GetMapping("/turma/{codigo}/{ano}/{semestre}")
    public ResponseEntity<List<InscricaoDTO>> listarInscricoesPorTurma(@PathVariable int codigo,
                                                                     @PathVariable int ano, 
                                                                     @PathVariable int semestre) {
        try {
            List<InscricaoDTO> inscricoes = inscricaoRepositoryService.listarInscricoesPorTurma(codigo);
            return ResponseEntity.ok(inscricoes);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}