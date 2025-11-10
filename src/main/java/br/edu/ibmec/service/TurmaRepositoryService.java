package br.edu.ibmec.service;

import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.DisciplinaRepository;
import br.edu.ibmec.repository.TurmaRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TurmaRepositoryService {

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Transactional(readOnly = true)
    public List<TurmaDTO> listarTurmas() {
        return turmaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TurmaDTO buscarTurma(int codigo, int ano, int semestre) throws DaoException {
        Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(codigo, ano, semestre);
        if (turma == null) {
            throw new DaoException("Turma não encontrada");
        }
        return convertToDTO(turma);
    }

    public void cadastrarTurma(TurmaDTO dto) throws ServiceException, DaoException {
        validar(dto);
        if (turmaRepository.findByCodigoAndAnoAndSemestre(dto.getCodigo(), dto.getAno(), dto.getSemestre()) != null) {
            throw new ServiceException("Turma já cadastrada para este período");
        }
        Disciplina disciplina = obterDisciplina(dto.getDisciplina());
        Turma turma = new Turma();
        turma.setCodigo(dto.getCodigo());
        turma.setAno(dto.getAno());
        turma.setSemestre(dto.getSemestre());
        turma.setDisciplina(disciplina);
        turmaRepository.save(turma);
    }

    public void alterarTurma(TurmaDTO dto) throws ServiceException, DaoException {
        validar(dto);
        Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(dto.getCodigo(), dto.getAno(), dto.getSemestre());
        if (turma == null) {
            throw new DaoException("Turma não encontrada");
        }
        turma.setDisciplina(obterDisciplina(dto.getDisciplina()));
        turmaRepository.save(turma);
    }

    public void removerTurma(int codigo, int ano, int semestre) throws DaoException {
        Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(codigo, ano, semestre);
        if (turma == null) {
            throw new DaoException("Turma não encontrada");
        }
        turmaRepository.delete(turma);
    }

    private void validar(TurmaDTO dto) throws ServiceException {
        if (dto.getCodigo() < 1) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (dto.getAno() < 1900 || dto.getAno() > 2100) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if (dto.getSemestre() < 1 || dto.getSemestre() > 2) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }
        if (dto.getDisciplina() == null || dto.getDisciplina() < 1) {
            throw new ServiceException("Disciplina é obrigatória");
        }
    }

    private Disciplina obterDisciplina(int codigo) throws DaoException {
        return disciplinaRepository.findById(codigo)
                .orElseThrow(() -> new DaoException("Disciplina com código " + codigo + " não encontrada"));
    }

    private TurmaDTO convertToDTO(Turma turma) {
        return TurmaDTO.builder()
                .codigo(turma.getCodigo())
                .ano(turma.getAno())
                .semestre(turma.getSemestre())
                .disciplina(turma.getDisciplina() != null ? turma.getDisciplina().getCodigo() : null)
                .build();
    }
}
