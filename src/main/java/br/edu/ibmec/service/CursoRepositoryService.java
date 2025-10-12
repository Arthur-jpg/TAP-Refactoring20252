package br.edu.ibmec.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

/** Serviço de Curso usando Spring Data JPA Repository. */
@Service("cursoRepositoryService")
@Transactional
public class CursoRepositoryService {
    
    private static final int CODIGO_MINIMO = 1;
    private static final int CODIGO_MAXIMO = 99;
    private static final int NOME_TAMANHO_MAXIMO = 20;

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public CursoDTO buscarCurso(int codigo) throws DaoException {
        Curso curso = cursoRepository.findByCodigoCurso(codigo);
        if (curso == null) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        
        return convertToDTO(curso);
    }

    @Transactional(readOnly = true)
    public Collection<Curso> listarCursos() throws DaoException {
        return cursoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursosCompletos() throws DaoException {
        List<Curso> cursos = cursoRepository.findAll();
        List<CursoDTO> cursosDTO = new ArrayList<>();
        
        for (Curso curso : cursos) {
            cursosDTO.add(convertToDTO(curso));
        }
        
        return cursosDTO;
    }

    @Transactional
    public void cadastrarCurso(CursoDTO cursoDTO) throws ServiceException, DaoException {
        validarCursoDTO(cursoDTO);

        if (cursoRepository.existsByCodigoCurso(cursoDTO.getCodigo())) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_DUPLICADO);
        }

        Curso curso = new Curso(cursoDTO.getCodigo(), cursoDTO.getNome().trim());
        cursoRepository.save(curso);
    }

    @Transactional
    public void alterarCurso(CursoDTO cursoDTO) throws ServiceException, DaoException {
        validarCursoDTO(cursoDTO);

        Optional<Curso> cursoOpt = cursoRepository.findById(cursoDTO.getCodigo());
        if (cursoOpt.isEmpty()) {
            throw new DaoException("Curso com código " + cursoDTO.getCodigo() + " não encontrado");
        }

        Curso curso = cursoOpt.get();
        curso.setNomeCurso(cursoDTO.getNome().trim());
        cursoRepository.save(curso);
    }

    @Transactional
    public void removerCurso(int codigo) throws DaoException {
        if (!cursoRepository.existsById(codigo)) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        
        cursoRepository.deleteById(codigo);
    }

    // Métodos privados - Clean Code: extrair validações e lógica repetida
    private void validarCursoDTO(CursoDTO cursoDTO) throws ServiceException {
        if (cursoDTO.getCodigo() < CODIGO_MINIMO || cursoDTO.getCodigo() > CODIGO_MAXIMO) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (cursoDTO.getNome() == null || cursoDTO.getNome().trim().isEmpty()
            || cursoDTO.getNome().trim().length() > NOME_TAMANHO_MAXIMO) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
    }

    private CursoDTO convertToDTO(Curso curso) {
        return CursoDTO.builder()
            .codigo(curso.getCodigoCurso())
            .nome(curso.getNomeCurso())
            .build();
    }
}