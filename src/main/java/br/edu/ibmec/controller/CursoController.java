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

import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.CursoRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller REST para gerenciamento de cursos.
 * Migrado de JAX-RS para Spring MVC seguindo Clean Code.
 */
@RestController
@RequestMapping("/api/curso")
@Tag(name = "Cursos", description = "API para gerenciamento de cursos do sistema universitário")
public class CursoController {

    @Autowired
    private CursoRepositoryService cursoService;

    @Operation(summary = "Buscar curso", description = "Busca um curso específico pelo código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Curso encontrado",
                content = @Content(schema = @Schema(implementation = CursoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    @GetMapping("/{codigo}")
    public ResponseEntity<CursoDTO> buscarCurso(
            @Parameter(description = "Código do curso", example = "1")
            @PathVariable int codigo) {
        try {
            CursoDTO cursoDTO = cursoService.buscarCurso(codigo);
            return ResponseEntity.ok(cursoDTO);
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cadastrar curso", description = "Cadastra um novo curso no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<String> cadastrarCurso(
            @Parameter(description = "Dados do curso")
            @RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.cadastrarCurso(cursoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Curso cadastrado com sucesso");
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
     * Atualiza curso existente.
     * PUT /api/curso
     */
    @PutMapping
    public ResponseEntity<String> alterarCurso(@RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.alterarCurso(cursoDTO);
            return ResponseEntity.ok("Curso alterado com sucesso");
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
     * Remove curso por código.
     * DELETE /api/curso/{codigo}
     */
    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> removerCurso(@PathVariable int codigo) {
        try {
            cursoService.removerCurso(codigo);
            return ResponseEntity.ok("Curso removido com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lista todos os cursos.
     * GET /api/curso
     */
    @GetMapping
    public ResponseEntity<List<String>> listarCursos() {
        try {
            List<String> nomes = new ArrayList<>();
            for (Iterator<Curso> it = cursoService.listarCursos().iterator(); it.hasNext();) {
                Curso curso = it.next();
                nomes.add(curso.getNome());
            }
            return ResponseEntity.ok(nomes);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Lista todos os cursos completos (versão melhorada).
     * GET /api/curso/completos
     */
    @GetMapping("/completos")
    public ResponseEntity<List<Curso>> listarCursosCompletos() {
        try {
            List<Curso> cursos = new ArrayList<>(cursoService.listarCursos());
            return ResponseEntity.ok(cursos);
        } catch (DaoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}