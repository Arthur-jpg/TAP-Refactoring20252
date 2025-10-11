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

import jakarta.validation.Valid;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.service.AlunoRepositoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/aluno")
@Tag(name = "Alunos", description = "API para gerenciamento de alunos do sistema universitário")
public class AlunoController {

    @Autowired
    private AlunoRepositoryService alunoService;
    @Operation(summary = "Buscar aluno", description = "Busca um aluno específico pela matrícula")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlunoDTO.class))),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado",
                content = @Content)
    })
    @GetMapping("/{matricula}")
    public ResponseEntity<AlunoDTO> buscarAlunoPorMatricula(
            @Parameter(description = "Matrícula do aluno", required = true, example = "12345")
            @PathVariable int matricula) {
        try {
            AlunoDTO alunoEncontrado = alunoService.buscarAluno(matricula);
            return ResponseEntity.ok(alunoEncontrado);
        } catch (DaoException daoException) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Cadastrar aluno", description = "Cadastra um novo aluno no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso",
                content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(mediaType = "text/plain"))
    })
    @PostMapping
    public ResponseEntity<String> criarNovoAluno(
            @Parameter(description = "Dados do aluno a ser cadastrado", required = true)
            @Valid @RequestBody AlunoDTO alunoDTO) {
        try {
            alunoService.cadastrarAluno(alunoDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Aluno cadastrado com sucesso");
        } catch (ServiceException serviceException) {
            return tratarServiceException(serviceException);
        } catch (DaoException daoException) {
            return tratarDaoException();
        }
    }


    @Operation(summary = "Alterar aluno", description = "Altera os dados de um aluno existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno alterado com sucesso",
                content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos",
                content = @Content(mediaType = "text/plain"))
    })
    @PutMapping
    public ResponseEntity<String> atualizarDadosDoAluno(
            @Parameter(description = "Dados atualizados do aluno", required = true)
            @RequestBody AlunoDTO alunoDTO) {
        try {
            alunoService.alterarAluno(alunoDTO);
            return ResponseEntity.ok("Aluno alterado com sucesso");
        } catch (ServiceException serviceException) {
            return tratarServiceException(serviceException);
        } catch (DaoException daoException) {
            return tratarDaoException();
        }
    }


    @Operation(summary = "Remover aluno", description = "Remove um aluno do sistema pela matrícula")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Aluno removido com sucesso",
                content = @Content(mediaType = "text/plain")),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado",
                content = @Content)
    })
    @DeleteMapping("/{matricula}")
    public ResponseEntity<String> excluirAlunoPorMatricula(
            @Parameter(description = "Matrícula do aluno a ser removido", required = true, example = "12345")
            @PathVariable int matricula) {
        try {
            alunoService.removerAluno(matricula);
            return ResponseEntity.ok("Aluno removido com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Listar nomes dos alunos", description = "Lista apenas os nomes de todos os alunos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de nomes obtida com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<String>> obterNomesDeAlunosMatriculados() {
        try {
            List<String> nomesDeAlunos = new ArrayList<>();
            for (Iterator<Aluno> iteratorDeAlunos = alunoService.listarAlunos().iterator(); iteratorDeAlunos.hasNext();) {
                Aluno alunoAtual = iteratorDeAlunos.next();
                nomesDeAlunos.add(alunoAtual.getNomeCompleto());
            }
            return ResponseEntity.ok(nomesDeAlunos);
        } catch (DaoException daoException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Operation(summary = "Listar alunos completos", description = "Lista todos os alunos com dados completos (DTOs)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de alunos obtida com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AlunoDTO.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                content = @Content)
    })
    @GetMapping("/completos")
    public ResponseEntity<List<AlunoDTO>> obterDadosCompletosDeAlunos() {
        try {
            List<AlunoDTO> dadosCompletosDeAlunos = alunoService.listarAlunosCompletos();
            return ResponseEntity.ok(dadosCompletosDeAlunos);
        } catch (DaoException daoException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ResponseEntity<String> tratarServiceException(ServiceException serviceException) {
        if (isCodigoInvalido(serviceException)) {
            return ResponseEntity.badRequest().body("Código inválido");
        }
        if (isNomeInvalido(serviceException)) {
            return ResponseEntity.badRequest().body("Nome inválido");
        }
        return ResponseEntity.badRequest().body(serviceException.getMessage());
    }

    private ResponseEntity<String> tratarDaoException() {
        return ResponseEntity.badRequest().body("Erro no banco de dados");
    }

    private boolean isCodigoInvalido(ServiceException serviceException) {
        return serviceException.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO;
    }

    private boolean isNomeInvalido(ServiceException serviceException) {
        return serviceException.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO;
    }
}