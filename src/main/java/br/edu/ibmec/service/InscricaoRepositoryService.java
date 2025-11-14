package br.edu.ibmec.service;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.repository.InscricaoRepository;
import br.edu.ibmec.repository.TurmaRepository;
import br.edu.ibmec.service.calculo.CalculoNotaResultado;
import br.edu.ibmec.service.calculo.CalculoNotaStrategy;
import br.edu.ibmec.service.calculo.TipoCalculoNota;
import br.edu.ibmec.service.factory.InscricaoFactory;
import br.edu.ibmec.service.validation.InscricaoValidacaoStrategy;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InscricaoRepositoryService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private List<InscricaoValidacaoStrategy> inscricaoValidacoes;
    
    @Autowired
    private InscricaoFactory inscricaoFactory;
    
    @Autowired(required = false)
    private List<CalculoNotaStrategy> calculoNotaStrategies;
    
    private Map<TipoCalculoNota, CalculoNotaStrategy> calculoNotaPorTipo;
    
    @PostConstruct
    void configurarCalculoNota() {
        if (calculoNotaStrategies != null) {
            calculoNotaPorTipo = calculoNotaStrategies.stream()
                    .collect(Collectors.toMap(CalculoNotaStrategy::getTipo, strategy -> strategy));
        }
    }

    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoes() {
        return inscricaoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoesPorAluno(int matricula) {
        return inscricaoRepository.findByAlunoMatricula(matricula).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoesPorTurma(int codigo, int ano, int semestre) {
        return inscricaoRepository.findByTurmaCodigoAndTurmaAnoAndTurmaSemestre(codigo, ano, semestre).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InscricaoDTO buscarInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        Inscricao inscricao = inscricaoRepository.findByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(matricula, codigo, ano, semestre)
                .orElseThrow(() -> new DaoException("Inscrição não encontrada"));
        return convertToDTO(inscricao);
    }

    public void cadastrarInscricao(InscricaoDTO dto) throws ServiceException, DaoException {
        aplicarValidacoes(dto);
        if (dto.getId() == null || dto.getId() < 1) {
            throw new ServiceException("Id da inscrição é obrigatório");
        }
        if (inscricaoRepository.existsById(dto.getId())) {
            throw new ServiceException("Já existe inscrição com id " + dto.getId());
        }
        if (inscricaoRepository.existsByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(dto.getAlunoMatricula(), dto.getTurmaCodigo(), dto.getAno(), dto.getSemestre())) {
            throw new ServiceException("Aluno já inscrito nesta turma");
        }
        Aluno aluno = alunoRepository.findById(dto.getAlunoMatricula())
                .orElseThrow(() -> new DaoException("Aluno não encontrado"));
        Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(dto.getTurmaCodigo(), dto.getAno(), dto.getSemestre());
        if (turma == null) {
            throw new DaoException("Turma não encontrada");
        }
        Inscricao inscricao = inscricaoFactory.criarInscricao(dto, aluno, turma);
        aplicarNotas(inscricao, dto);
        inscricaoRepository.save(inscricao);
    }

    public void removerInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        Inscricao inscricao = inscricaoRepository.findByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(matricula, codigo, ano, semestre)
                .orElseThrow(() -> new DaoException("Inscrição não encontrada"));
        inscricaoRepository.delete(inscricao);
    }

    private void aplicarValidacoes(InscricaoDTO dto) throws ServiceException {
        if (inscricaoValidacoes == null) {
            return;
        }
        for (InscricaoValidacaoStrategy validacao : inscricaoValidacoes) {
            validacao.validar(dto);
        }
    }
    
    private void aplicarNotas(Inscricao inscricao, InscricaoDTO dto) throws ServiceException {
        inscricao.setNotaAv1(dto.getNotaAv1());
        inscricao.setNotaAv2(dto.getNotaAv2());
        inscricao.setNotaRecuperacao(dto.getNotaRecuperacao());
        if (dto.getTipoCalculoNota() == null) {
            return;
        }
        if (calculoNotaPorTipo == null) {
            throw new ServiceException("Nenhuma estratégia de cálculo de nota configurada");
        }
        TipoCalculoNota tipo = dto.getTipoCalculoNota();
        CalculoNotaStrategy strategy = Optional.ofNullable(calculoNotaPorTipo.get(tipo))
                .orElse(calculoNotaPorTipo.get(TipoCalculoNota.MEDIA_SIMPLES));
        if (strategy == null) {
            throw new ServiceException("Estratégia de cálculo de nota não encontrada");
        }
        CalculoNotaResultado resultado = strategy.calcular(inscricao);
        inscricao.setMediaFinal(resultado.getMediaFinal());
        inscricao.setStatusAprovacao(resultado.getStatus());
    }

    private InscricaoDTO convertToDTO(Inscricao inscricao) {
        return InscricaoDTO.builder()
                .id(inscricao.getId())
                .alunoMatricula(inscricao.getAluno() != null ? inscricao.getAluno().getMatricula() : 0)
                .turmaCodigo(inscricao.getTurma() != null ? inscricao.getTurma().getCodigo() : 0)
                .ano(inscricao.getTurma() != null ? inscricao.getTurma().getAno() : 0)
                .semestre(inscricao.getTurma() != null ? inscricao.getTurma().getSemestre() : 0)
                .notaAv1(inscricao.getNotaAv1())
                .notaAv2(inscricao.getNotaAv2())
                .notaRecuperacao(inscricao.getNotaRecuperacao())
                .mediaFinal(inscricao.getMediaFinal())
                .statusAprovacao(inscricao.getStatusAprovacao())
                .build();
    }
}
