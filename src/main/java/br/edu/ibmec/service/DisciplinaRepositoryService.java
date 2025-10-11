package br.edu.ibmec.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.CursoRepository;
import br.edu.ibmec.repository.DisciplinaRepository;

/**
 * Serviço para Disciplina usando Spring Data JPA Repository
 */
@Service("disciplinaRepositoryService")
@Transactional
public class DisciplinaRepositoryService {
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;
    
    @Autowired
    private CursoRepository cursoRepository;

    public DisciplinaDTO buscarDisciplina(int codigo) throws DaoException {
        Disciplina disciplina = disciplinaRepository.findByCodigo(codigo);
        if (disciplina == null) {
            throw new DaoException("Disciplina com código " + codigo + " não encontrada");
        }
        
        return convertToDTO(disciplina);
    }

    @Transactional(readOnly = true)
    public Collection<Disciplina> listarDisciplinas() throws DaoException {
        return disciplinaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDTO> listarDisciplinasCompletas() throws DaoException {
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        List<DisciplinaDTO> disciplinasDTO = new ArrayList<>();
        
        for (Disciplina disciplina : disciplinas) {
            disciplinasDTO.add(convertToDTO(disciplina));
        }
        
        return disciplinasDTO;
    }

    @Transactional
    public void cadastrarDisciplina(DisciplinaDTO disciplinaDTO) throws ServiceException, DaoException {
        if (disciplinaDTO.getCodigo() < 1 || disciplinaDTO.getCodigo() > 99) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (disciplinaDTO.getNome() == null || disciplinaDTO.getNome().trim().isEmpty() ||
            disciplinaDTO.getNome().trim().length() > 20) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        if (disciplinaRepository.existsByCodigo(disciplinaDTO.getCodigo())) {
            throw new ServiceException("Disciplina com código " + disciplinaDTO.getCodigo() + " já existe");
        }

        Curso curso = null;
        if (disciplinaDTO.getCurso() > 0) {
            curso = cursoRepository.findByCodigoCurso(disciplinaDTO.getCurso());
            if (curso == null) {
                throw new DaoException("Curso com código " + disciplinaDTO.getCurso() + " não encontrado");
            }
        }

        Disciplina disciplina = convertToEntity(disciplinaDTO, curso);
        disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void alterarDisciplina(DisciplinaDTO disciplinaDTO) throws ServiceException, DaoException {
        if (disciplinaDTO.getCodigo() < 1 || disciplinaDTO.getCodigo() > 99) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (disciplinaDTO.getNome() == null || disciplinaDTO.getNome().trim().isEmpty() ||
            disciplinaDTO.getNome().trim().length() > 20) {
            throw new ServiceException(ServiceException.ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Optional<Disciplina> disciplinaOpt = disciplinaRepository.findById(disciplinaDTO.getCodigo());
        if (disciplinaOpt.isEmpty()) {
            throw new DaoException("Disciplina com código " + disciplinaDTO.getCodigo() + " não encontrada");
        }

        Curso curso = null;
        if (disciplinaDTO.getCurso() > 0) {
            curso = cursoRepository.findByCodigoCurso(disciplinaDTO.getCurso());
            if (curso == null) {
                throw new DaoException("Curso com código " + disciplinaDTO.getCurso() + " não encontrado");
            }
        }

        Disciplina disciplina = disciplinaOpt.get();
        disciplina.setNome(disciplinaDTO.getNome().trim());
        disciplina.setCurso(curso);
        
        disciplinaRepository.save(disciplina);
    }

    @Transactional
    public void removerDisciplina(int codigo) throws DaoException {
        if (!disciplinaRepository.existsById(codigo)) {
            throw new DaoException("Disciplina com código " + codigo + " não encontrada");
        }
        
        disciplinaRepository.deleteById(codigo);
    }

    private DisciplinaDTO convertToDTO(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setCodigo(disciplina.getCodigo());
        dto.setNome(disciplina.getNome());
        
        if (disciplina.getCurso() != null) {
            dto.setCurso(disciplina.getCurso().getCodigoCurso());
        }
        
        return dto;
    }

    private Disciplina convertToEntity(DisciplinaDTO dto, Curso curso) {
        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(dto.getCodigo());
        disciplina.setNome(dto.getNome().trim());
        disciplina.setCurso(curso);
        
        return disciplina;
    }
}