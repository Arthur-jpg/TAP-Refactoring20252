package br.edu.ibmec.service;

import java.util.Collection;
import java.util.Optional;

import br.edu.ibmec.dao.DisciplinaDAO;
import br.edu.ibmec.dao.CursoDAO;
import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DisciplinaService {
    
    @Autowired
    private DisciplinaDAO disciplinaDAO;
    
    @Autowired
    private CursoDAO cursoDAO;

    public DisciplinaDTO buscarDisciplina(int codigo) throws DaoException {
        try {
            Optional<Disciplina> disciplinaOpt = disciplinaDAO.findByCodigo(codigo);
            if (disciplinaOpt.isEmpty()) {
                throw new DaoException("Disciplina com código " + codigo + " não encontrada");
            }
            
            Disciplina disciplina = disciplinaOpt.get();
            DisciplinaDTO disciplinaDTO = new DisciplinaDTO(
                    disciplina.getCodigo(), 
                    disciplina.getNome(),
                    disciplina.getCurso() != null ? disciplina.getCurso().getCodigo() : 0);
            return disciplinaDTO;
        } catch (DaoException e) {
            throw new DaoException("Erro ao buscar disciplina: " + e.getMessage());
        }
    }

    public Collection<Disciplina> listarDisciplinas() throws DaoException {
        return disciplinaDAO.findAll();
    }

    public void cadastrarDisciplina(DisciplinaDTO disciplinaDTO)
            throws ServiceException, DaoException {
        // Validações
        if ((disciplinaDTO.getCodigo() < 1) || (disciplinaDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((disciplinaDTO.getNome().length() < 1) || (disciplinaDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        // Verificar se o curso existe
        Optional<Curso> cursoOpt = cursoDAO.findByCodigo(disciplinaDTO.getCurso());
        if (cursoOpt.isEmpty()) {
            throw new ServiceException("Curso não encontrado");
        }

        Disciplina disciplina = new Disciplina(
                disciplinaDTO.getCodigo(),
                disciplinaDTO.getNome(), 
                cursoOpt.get());

        try {
            disciplinaDAO.save(disciplina);
        } catch (DaoException e) {
            throw new DaoException("Erro ao cadastrar disciplina: " + e.getMessage());
        }
    }

    public void alterarDisciplina(DisciplinaDTO disciplinaDTO)
            throws ServiceException, DaoException {
        // Validações
        if ((disciplinaDTO.getCodigo() < 1) || (disciplinaDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((disciplinaDTO.getNome().length() < 1) || (disciplinaDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        // Verificar se o curso existe
        Optional<Curso> cursoOpt = cursoDAO.findByCodigo(disciplinaDTO.getCurso());
        if (cursoOpt.isEmpty()) {
            throw new ServiceException("Curso não encontrado");
        }

        Disciplina disciplina = new Disciplina(
                disciplinaDTO.getCodigo(),
                disciplinaDTO.getNome(), 
                cursoOpt.get());

        try {
            disciplinaDAO.update(disciplina);
        } catch (DaoException e) {
            throw new DaoException("Erro ao alterar disciplina: " + e.getMessage());
        }
    }

    public void removerDisciplina(int codigo) throws DaoException {
        try {
            disciplinaDAO.deleteByCodigo(codigo);
        } catch (DaoException e) {
            throw new DaoException("Erro ao remover disciplina: " + e.getMessage());
        }
    }

}