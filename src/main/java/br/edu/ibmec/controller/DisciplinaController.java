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

import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.DisciplinaRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para gerenciamento de disciplinas.
 * Migrado de JAX-RS para Spring MVC seguindo Clean Code.
 */
@RestController
@RequestMapping("/api/disciplina")
@Tag(name = "Disciplinas", description = "API para gerenciamento de disciplinas do sistema universitário")
public class DisciplinaController {

    @Autowired
    private DisciplinaRepositoryService disciplinaService;

    /**
     * Busca disciplina por código.
     * GET /api/disciplina/{codigo}
     */
    @Operation(summary = "Buscar disciplina", description = "Busca uma disciplina pelo código")
    @GetMapping("/{codigo}")
    public ResponseEntity<DisciplinaDTO> buscarDisciplina(
            @Parameter(description = "Código da disciplina") @PathVariable int codigo) {
        try {
            DisciplinaDTO disciplinaDTO = disciplinaService.buscarDisciplina(codigo);
            return ResponseEntity.ok(disciplinaDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cadastra nova disciplina.
     * POST /api/disciplina
     */
    @PostMapping
    public ResponseEntity<String> cadastrarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            disciplinaService.cadastrarDisciplina(disciplinaDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Disciplina cadastrada com sucesso");
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Código inválido");
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Nome inválido");
            }
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .body("Erro no banco de dados");
        }
    }

    /**
     * Atualiza disciplina existente.
     * PUT /api/disciplina
     */
    @PutMapping
    public ResponseEntity<String> alterarDisciplina(@RequestBody DisciplinaDTO disciplinaDTO) {
        try {
            disciplinaService.alterarDisciplina(disciplinaDTO);
            return ResponseEntity.ok("Disciplina alterada com sucesso");
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Código inválido");
            }
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO) {
                return ResponseEntity.badRequest()
                        .body("Nome inválido");
            }
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        } catch (DaoException e) {
            return ResponseEntity.badRequest()
                    .body("Erro no banco de dados");
        }
    }

    /**
     * Remove disciplina por código.
     * DELETE /api/disciplina/{codigo}
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerDisciplina(@PathVariable int codigo) {
        try {
            disciplinaService.removerDisciplina(codigo);
            return ResponseEntity.ok("Disciplina removida com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todas as disciplinas.
     * GET /api/disciplina
     */
    @GetMapping
    public ResponseEntity<List<String>> listarDisciplinas() {
        try {
            List<String> nomes = new ArrayList<>();
            for (Iterator<Disciplina> it = disciplinaService.listarDisciplinas().iterator(); it.hasNext();) {
                Disciplina disciplina = it.next();
                nomes.add(disciplina.getNome());
            }
            return ResponseEntity.ok(nomes);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista todas as disciplinas completas (versão melhorada).
     * GET /api/disciplina/completas
     */
    @GetMapping("/completas")
    public ResponseEntity<List<Disciplina>> listarDisciplinasCompletas() {
        try {
            List<Disciplina> disciplinas = new ArrayList<>(disciplinaService.listarDisciplinas());
            return ResponseEntity.ok(disciplinas);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}