package br.edu.ibmec.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.CursoRepository;

/**
 * Novo serviço para Curso usando Spring Data JPA Repository
 * Substituirá o CursoService antigo para resolver problemas de transação
 */
@Service("cursoRepositoryService")
@Transactional
public class CursoRepositoryService {
    
    @Autowired
    private CursoRepository cursoRepository;

    public CursoDTO buscarCurso(int codigo) throws DaoException {
        Curso curso = cursoRepository.findByCodigo(codigo);
        if (curso == null) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        
        return new CursoDTO(curso.getCodigo(), curso.getNome());
    }

    @Transactional(readOnly = true)
    public Collection<Curso> listarCursos() throws DaoException {
        return cursoRepository.findAll();
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

        // Verifica se já existe
        if (cursoRepository.existsByCodigo(cursoDTO.getCodigo())) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_DUPLICADO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome().trim());
        cursoRepository.save(curso);
    }

    @Transactional
    public void alterarCurso(CursoDTO cursoDTO) throws ServiceException, DaoException {
        // Validações
        if ((cursoDTO.getCodigo() < 1) || (cursoDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (cursoDTO.getNome() == null || cursoDTO.getNome().trim().isEmpty() || 
            cursoDTO.getNome().length() > 100) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        // Verifica se existe
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoDTO.getCodigo());
        if (cursoOpt.isEmpty()) {
            throw new DaoException("Curso com código " + cursoDTO.getCodigo() + " não encontrado");
        }

        Curso curso = cursoOpt.get();
        curso.setNome(cursoDTO.getNome().trim());
        cursoRepository.save(curso);
    }

    @Transactional
    public void removerCurso(int codigo) throws DaoException {
        if (!cursoRepository.existsById(codigo)) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        
        cursoRepository.deleteById(codigo);
    }
}