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
import java.util.List;
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
        validar(dto);
        if (inscricaoRepository.existsByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(dto.getAlunoMatricula(), dto.getTurmaCodigo(), dto.getAno(), dto.getSemestre())) {
            throw new ServiceException("Aluno já inscrito nesta turma");
        }
        Aluno aluno = alunoRepository.findById(dto.getAlunoMatricula())
                .orElseThrow(() -> new DaoException("Aluno não encontrado"));
        Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(dto.getTurmaCodigo(), dto.getAno(), dto.getSemestre());
        if (turma == null) {
            throw new DaoException("Turma não encontrada");
        }
        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(aluno);
        inscricao.setTurma(turma);
        inscricaoRepository.save(inscricao);
    }

    public void removerInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        Inscricao inscricao = inscricaoRepository.findByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(matricula, codigo, ano, semestre)
                .orElseThrow(() -> new DaoException("Inscrição não encontrada"));
        inscricaoRepository.delete(inscricao);
    }

    private void validar(InscricaoDTO dto) throws ServiceException {
        if (dto.getAlunoMatricula() < 1) {
            throw new ServiceException("Matrícula do aluno é obrigatória");
        }
        if (dto.getTurmaCodigo() < 1) {
            throw new ServiceException("Código da turma é obrigatório");
        }
        if (dto.getAno() < 1900 || dto.getAno() > 2100) {
            throw new ServiceException("Ano inválido");
        }
        if (dto.getSemestre() < 1 || dto.getSemestre() > 2) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }
    }

    private InscricaoDTO convertToDTO(Inscricao inscricao) {
        return InscricaoDTO.builder()
                .id(inscricao.getId())
                .alunoMatricula(inscricao.getAluno() != null ? inscricao.getAluno().getMatricula() : 0)
                .turmaCodigo(inscricao.getTurma() != null ? inscricao.getTurma().getCodigo() : 0)
                .ano(inscricao.getTurma() != null ? inscricao.getTurma().getAno() : 0)
                .semestre(inscricao.getTurma() != null ? inscricao.getTurma().getSemestre() : 0)
                .build();
    }
}
