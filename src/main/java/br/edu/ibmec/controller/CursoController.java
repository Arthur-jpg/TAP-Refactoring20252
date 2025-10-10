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
    public ResponseEntity<CursoDTO> buscarCursoPorCodigo(
            @Parameter(description = "Código do curso", example = "1")
            @PathVariable int codigo) {
        try {
            CursoDTO cursoEncontrado = cursoService.buscarCurso(codigo);
            return ResponseEntity.ok(cursoEncontrado);
        } catch (DaoException daoException) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cadastrar curso", description = "Cadastra um novo curso no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<String> criarNovoCurso(
            @Parameter(description = "Dados do curso")
            @RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.cadastrarCurso(cursoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Curso cadastrado com sucesso");
        } catch (ServiceException serviceException) {
            return tratarExcecaoDeServico(serviceException);
        } catch (DaoException daoException) {
            return tratarExcecaoDeBancoDeDados();
        }
    }


    @PutMapping
    public ResponseEntity<String> atualizarDadosDoCurso(@RequestBody CursoDTO cursoDTO) {
        try {
            cursoService.alterarCurso(cursoDTO);
            return ResponseEntity.ok("Curso alterado com sucesso");
        } catch (ServiceException serviceException) {
            return tratarExcecaoDeServico(serviceException);
        } catch (DaoException daoException) {
            return tratarExcecaoDeBancoDeDados();
        }
    }


    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> excluirCursoPorCodigo(@PathVariable int codigo) {
        try {
            cursoService.removerCurso(codigo);
            return ResponseEntity.ok("Curso removido com sucesso");
        } catch (DaoException daoException) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<String>> obterNomesDeCursosDisponiveis() {
        try {
            List<String> nomesDeCursos = new ArrayList<>();
            for (Iterator<Curso> iteratorDeCursos = cursoService.listarCursos().iterator(); iteratorDeCursos.hasNext();) {
                Curso cursoAtual = iteratorDeCursos.next();
                nomesDeCursos.add(cursoAtual.obterNomeCurso());
            }
            return ResponseEntity.ok(nomesDeCursos);
        } catch (DaoException daoException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/completos")
    public ResponseEntity<List<CursoDTO>> obterDadosCompletosDeCursos() {
        try {
            List<CursoDTO> dadosCompletosDeCursos = cursoService.listarCursosCompletos();
            return ResponseEntity.ok(dadosCompletosDeCursos);
        } catch (DaoException daoException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<String> tratarExcecaoDeServico(ServiceException serviceException) {
        if (isCodigoInvalido(serviceException)) {
            return ResponseEntity.badRequest().body("Código inválido");
        }
        if (isNomeInvalido(serviceException)) {
            return ResponseEntity.badRequest().body("Nome inválido");
        }
        return ResponseEntity.badRequest().body(serviceException.getMessage());
    }

    private boolean isCodigoInvalido(ServiceException serviceException) {
        return serviceException.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO;
    }

    private boolean isNomeInvalido(ServiceException serviceException) {
        return serviceException.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO;
    }

    private ResponseEntity<String> tratarExcecaoDeBancoDeDados() {
        return ResponseEntity.badRequest().body("Erro no banco de dados");
    }
}