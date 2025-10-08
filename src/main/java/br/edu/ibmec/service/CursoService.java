package br.edu.ibmec.service;

import java.util.Collection;
import java.util.Optional;

import br.edu.ibmec.dao.CursoDAO;
import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CursoService {
    
    @Autowired
    private CursoDAO cursoDAO;

    public CursoDTO buscarCurso(int codigo) throws DaoException {
        try {
            Optional<Curso> cursoOpt = cursoDAO.findByCodigo(codigo);
            if (cursoOpt.isEmpty()) {
                throw new DaoException("Curso com código " + codigo + " não encontrado");
            }
            
            Curso curso = cursoOpt.get();
            CursoDTO cursoDTO = new CursoDTO(curso.getCodigo(), curso.getNome());
            return cursoDTO;
        } catch (DaoException e) {
            throw new DaoException("Erro ao buscar curso: " + e.getMessage());
        }
    }

    public Collection<Curso> listarCursos() throws DaoException {
        return cursoDAO.findAll();
    }

    @Transactional
    public void cadastrarCurso(CursoDTO cursoDTO) throws ServiceException, DaoException {
        // Validações
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (cursoDTO.getNome() == null || cursoDTO.getNome().trim().isEmpty() || 
            cursoDTO.getNome().length() > 100) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome().trim());

        try {
            cursoDAO.save(curso);
        } catch (DaoException e) {
            // Re-throw DaoException sem wrapping adicional para evitar rollback
            throw e;
        }
    }

    public void alterarCurso(CursoDTO cursoDTO) throws ServiceException, DaoException {
        // Validações
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((cursoDTO.getNome().length() < 1) || (cursoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome());

        try {
            cursoDAO.update(curso);
        } catch (DaoException e) {
            throw new DaoException("Erro ao alterar curso: " + e.getMessage());
        }
    }

    public void removerCurso(int codigo) throws DaoException {
        try {
            cursoDAO.deleteByCodigo(codigo);
        } catch (DaoException e) {
            throw new DaoException("Erro ao remover curso: " + e.getMessage());
        }
    }
}