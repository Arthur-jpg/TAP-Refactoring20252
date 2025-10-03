package br.edu.ibmec.service;

import java.util.Collection;

import br.edu.ibmec.dao.EscolaDAO;
import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

public class TurmaService {
    private EscolaDAO dao;

    public TurmaService() {
        this.dao = EscolaDAO.getInstance();
    }

    public TurmaDTO buscarTurma(int codigo, int ano, int semestre)
            throws DaoException {
        try {
            TurmaDTO turmaDTO = new TurmaDTO(dao
                    .getTurma(codigo, ano, semestre).getCodigo(), dao.getTurma(
                    codigo, ano, semestre).getAno(), dao.getTurma(codigo, ano,
                    semestre).getSemestre(), dao
                    .getTurma(codigo, ano, semestre).getDisciplina()
                    .getCodigo());
            return turmaDTO;
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

    public Collection<Turma> listarTurmas() throws DaoException {
        return dao.getTurmas();
    }

    public void cadastrarTurma(TurmaDTO turmaDTO) throws ServiceException,
            DaoException {
        if ((turmaDTO.getCodigo() < 1) || (turmaDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((turmaDTO.getAno() < 1900) || (turmaDTO.getAno() > 2020)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Turma turma = new Turma(turmaDTO.getCodigo(), turmaDTO.getAno(),
                turmaDTO.getSemestre(), dao.getDisciplina(turmaDTO
                .getDisciplina()));

        try {
            dao.addTurma(turma);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void alterarCurso(TurmaDTO turmaDTO) throws ServiceException,
            DaoException {
        if ((turmaDTO.getCodigo() < 1) || (turmaDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((turmaDTO.getAno() < 1900) || (turmaDTO.getAno() > 2020)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Turma turma = new Turma(turmaDTO.getCodigo(), turmaDTO.getAno(),
                turmaDTO.getSemestre(), dao.getDisciplina(turmaDTO
                .getDisciplina()));

        try {
            dao.updateTurma(turma);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void removerTurma(int codigo, int ano, int semestre)
            throws DaoException {
        try {
            dao.removeTurma(codigo, ano, semestre);
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

}