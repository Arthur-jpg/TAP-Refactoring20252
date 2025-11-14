package br.edu.ibmec.controller;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.AlunoRepositoryService;
import br.edu.ibmec.service.MensalidadeService;
import br.edu.ibmec.service.mensalidade.TipoCalculoMensalidade;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aluno")
@Tag(name = "Alunos")
public class AlunoController {

    @Autowired
    private AlunoRepositoryService alunoService;
    
    @Autowired
    private MensalidadeService mensalidadeService;

    @GetMapping
    public ResponseEntity<List<AlunoDTO>> listarAlunos() {
        return ResponseEntity.ok(alunoService.listarAlunos());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<AlunoDTO> buscarAluno(@PathVariable int matricula) throws DaoException {
        return ResponseEntity.ok(alunoService.buscarAluno(matricula));
    }

    @PostMapping
    public ResponseEntity<String> cadastrarAluno(@Valid @RequestBody AlunoDTO alunoDTO) throws ServiceException {
        alunoService.cadastrarAluno(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Aluno cadastrado com sucesso");
    }

    @PutMapping
    public ResponseEntity<String> atualizarAluno(@Valid @RequestBody AlunoDTO alunoDTO) throws ServiceException, DaoException {
        alunoService.alterarAluno(alunoDTO);
        return ResponseEntity.ok("Aluno atualizado com sucesso");
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<String> removerAluno(@PathVariable int matricula) throws DaoException {
        alunoService.removerAluno(matricula);
        return ResponseEntity.ok("Aluno removido com sucesso");
    }

    @GetMapping("/{matricula}/mensalidade")
    public ResponseEntity<java.math.BigDecimal> calcularMensalidade(@PathVariable int matricula,
            @RequestParam(name = "tipo", required = false) TipoCalculoMensalidade tipo)
            throws DaoException, ServiceException {
        return ResponseEntity.ok(mensalidadeService.calcularMensalidade(matricula, tipo));
    }
}
