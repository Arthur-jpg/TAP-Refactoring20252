package service;

import java.util.Collection;

import dao.EscolaDAO;
import dto.DisciplinaDTO;
import entity.Disciplina;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

public class DisciplinaService {
    private EscolaDAO dao;

    public DisciplinaService() {
        this.dao = EscolaDAO.getInstance();
    }

    public DisciplinaDTO buscarDisciplina(int codigo) throws DaoException {
        try {
            DisciplinaDTO disciplinaDTO = new DisciplinaDTO(dao.getDisciplina(
                    codigo).getCodigo(), dao.getDisciplina(codigo).getNome(),
                    dao.getDisciplina(codigo).getCurso().getCodigo());
            return disciplinaDTO;
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

    public Collection<Disciplina> listarDisciplinas() {
        return dao.getDisciplinas();
    }

    public void cadastrarDisciplina(DisciplinaDTO disciplinaDTO)
            throws ServiceException, DaoException {
        if ((disciplinaDTO.getCodigo() < 1) || (disciplinaDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((disciplinaDTO.getNome().length() < 1)
                || (disciplinaDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Disciplina disciplina = new Disciplina(disciplinaDTO.getCodigo(),
                disciplinaDTO.getNome(), dao.getCurso(disciplinaDTO.getCurso()));

        try {
            dao.addDisciplina(disciplina);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void alterarDisciplina(DisciplinaDTO disciplinaDTO)
            throws ServiceException, DaoException {
        if ((disciplinaDTO.getCodigo() < 1) || (disciplinaDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((disciplinaDTO.getNome().length() < 1)
                || (disciplinaDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Disciplina disciplina = new Disciplina(disciplinaDTO.getCodigo(),
                disciplinaDTO.getNome(), dao.getCurso(disciplinaDTO.getCurso()));

        try {
            dao.updateDisciplina(disciplina);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void removerDisciplina(int codigo) throws DaoException {
        try {
            dao.removeDisciplina(codigo);
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

}