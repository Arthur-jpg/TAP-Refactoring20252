package br.edu.ibmec.service;

import java.util.Collection;
import java.util.Optional;

import br.edu.ibmec.dao.TurmaDAO;
import br.edu.ibmec.dao.DisciplinaDAO;
import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TurmaService {
    
    @Autowired
    private TurmaDAO turmaDAO;
    
    @Autowired
    private DisciplinaDAO disciplinaDAO;

    public TurmaDTO buscarTurma(int codigo, int ano, int semestre) throws DaoException {
        try {
            Optional<Turma> turmaOpt = turmaDAO.findByCodigoAnoSemestre(codigo, ano, semestre);
            if (turmaOpt.isEmpty()) {
                throw new DaoException("Turma " + codigo + "/" + ano + "/" + semestre + " não encontrada");
            }
            
            Turma turma = turmaOpt.get();
            TurmaDTO turmaDTO = new TurmaDTO(
                    turma.getCodigo(), 
                    turma.getAno(),
                    turma.getSemestre(), 
                    turma.getDisciplina() != null ? turma.getDisciplina().getCodigo() : 0);
            return turmaDTO;
        } catch (DaoException e) {
            throw new DaoException("Erro ao buscar turma: " + e.getMessage());
        }
    }

    public Collection<Turma> listarTurmas() throws DaoException {
        return turmaDAO.findAll();
    }

    public void cadastrarTurma(TurmaDTO turmaDTO) throws ServiceException, DaoException {
        // Validações
        if ((turmaDTO.getCodigo() < 1) || (turmaDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((turmaDTO.getAno() < 1900) || (turmaDTO.getAno() > 2100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if ((turmaDTO.getSemestre() < 1) || (turmaDTO.getSemestre() > 2)) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }

        // Verificar se a disciplina existe
        Optional<Disciplina> disciplinaOpt = disciplinaDAO.findByCodigo(turmaDTO.getDisciplina());
        if (disciplinaOpt.isEmpty()) {
            throw new ServiceException("Disciplina não encontrada");
        }

        Turma turma = new Turma(
                turmaDTO.getCodigo(), 
                turmaDTO.getAno(),
                turmaDTO.getSemestre(), 
                disciplinaOpt.get());

        try {
            turmaDAO.save(turma);
        } catch (DaoException e) {
            throw new DaoException("Erro ao cadastrar turma: " + e.getMessage());
        }
    }

    public void alterarCurso(TurmaDTO turmaDTO) throws ServiceException, DaoException {
        // Validações
        if ((turmaDTO.getCodigo() < 1) || (turmaDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((turmaDTO.getAno() < 1900) || (turmaDTO.getAno() > 2100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if ((turmaDTO.getSemestre() < 1) || (turmaDTO.getSemestre() > 2)) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }

        // Verificar se a disciplina existe
        Optional<Disciplina> disciplinaOpt = disciplinaDAO.findByCodigo(turmaDTO.getDisciplina());
        if (disciplinaOpt.isEmpty()) {
            throw new ServiceException("Disciplina não encontrada");
        }

        Turma turma = new Turma(
                turmaDTO.getCodigo(), 
                turmaDTO.getAno(),
                turmaDTO.getSemestre(), 
                disciplinaOpt.get());

        try {
            turmaDAO.update(turma);
        } catch (DaoException e) {
            throw new DaoException("Erro ao alterar turma: " + e.getMessage());
        }
    }

    public void removerTurma(int codigo, int ano, int semestre) throws DaoException {
        try {
            turmaDAO.deleteByCodigoAnoSemestre(codigo, ano, semestre);
        } catch (DaoException e) {
            throw new DaoException("Erro ao remover turma: " + e.getMessage());
        }
    }

}