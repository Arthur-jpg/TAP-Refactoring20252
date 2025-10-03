package service;

import java.util.Collection;

import dao.EscolaDAO;
import dto.CursoDTO;
import entity.Curso;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

public class CursoService {
    private EscolaDAO dao;

    public CursoService() {
        this.dao = EscolaDAO.getInstance();
    }

    public CursoDTO buscarCurso(int codigo) throws DaoException {
        try{
            CursoDTO cursoDTO = new CursoDTO(dao.getCurso(codigo).getCodigo(), dao
                    .getCurso(codigo).getNome());
            return cursoDTO;
        }
        catch(DaoException e)
        {
            throw new DaoException("");
        }
    }

    public Collection<Curso> listarCursos() {
        return dao.getCursos();
    }

    public void cadastrarCurso(CursoDTO cursoDTO) throws ServiceException,
            DaoException {
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((cursoDTO.getNome().length() < 1)
                || (cursoDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome());

        try {
            dao.addCurso(curso);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void alterarCurso(CursoDTO cursoDTO) throws ServiceException,
            DaoException {
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((cursoDTO.getNome().length() < 1)
                || (cursoDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome());

        try {
            dao.updateCurso(curso);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void removerCurso(int codigo) throws DaoException {
        try {
            dao.removeCurso(codigo);
        }
        catch(DaoException e)
        {
            throw new DaoException("");
        }
    }
}