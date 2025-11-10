package br.edu.ibmec.controller;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.InscricaoRepositoryService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscricao")
@Tag(name = "Inscrições")
public class InscricaoController {

    @Autowired
    private InscricaoRepositoryService inscricaoService;

    @GetMapping
    public ResponseEntity<List<InscricaoDTO>> listarInscricoes() {
        return ResponseEntity.ok(inscricaoService.listarInscricoes());
    }

    @GetMapping("/{matricula}/{codigo}/{ano}/{semestre}")
    public ResponseEntity<InscricaoDTO> buscarInscricao(@PathVariable int matricula,
                                                        @PathVariable int codigo,
                                                        @PathVariable int ano,
                                                        @PathVariable int semestre) {
        try {
            return ResponseEntity.ok(inscricaoService.buscarInscricao(matricula, codigo, ano, semestre));
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/aluno/{matricula}")
    public ResponseEntity<List<InscricaoDTO>> listarPorAluno(@PathVariable int matricula) {
        return ResponseEntity.ok(inscricaoService.listarInscricoesPorAluno(matricula));
    }

    @GetMapping("/turma/{codigo}/{ano}/{semestre}")
    public ResponseEntity<List<InscricaoDTO>> listarPorTurma(@PathVariable int codigo,
                                                             @PathVariable int ano,
                                                             @PathVariable int semestre) {
        return ResponseEntity.ok(inscricaoService.listarInscricoesPorTurma(codigo, ano, semestre));
    }

    @PostMapping
    public ResponseEntity<String> cadastrarInscricao(@Valid @RequestBody InscricaoDTO inscricaoDTO) {
        try {
            inscricaoService.cadastrarInscricao(inscricaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Inscrição cadastrada com sucesso");
        } catch (ServiceException | DaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{matricula}/{codigo}/{ano}/{semestre}")
    public ResponseEntity<String> removerInscricao(@PathVariable int matricula,
                                                   @PathVariable int codigo,
                                                   @PathVariable int ano,
                                                   @PathVariable int semestre) {
        try {
            inscricaoService.removerInscricao(matricula, codigo, ano, semestre);
            return ResponseEntity.ok("Inscrição removida com sucesso");
        } catch (DaoException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
