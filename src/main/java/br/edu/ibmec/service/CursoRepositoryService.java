package br.edu.ibmec.service;

import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.CursoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("cursoRepositoryService")
@Transactional
public class CursoRepositoryService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public CursoDTO buscarCurso(int codigo) throws DaoException {
        Curso curso = cursoRepository.findByCodigo(codigo);
        if (curso == null) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        return convertToDTO(curso);
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void cadastrarCurso(CursoDTO dto) throws ServiceException {
        validar(dto);
        if (cursoRepository.existsByCodigo(dto.getCodigo())) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_DUPLICADO);
        }
        Curso curso = new Curso();
        curso.setCodigo(dto.getCodigo());
        curso.setNome(dto.getNome());
        cursoRepository.save(curso);
    }

    public void alterarCurso(CursoDTO dto) throws ServiceException, DaoException {
        validar(dto);
        Optional<Curso> existente = cursoRepository.findById(dto.getCodigo());
        if (existente.isEmpty()) {
            throw new DaoException("Curso com código " + dto.getCodigo() + " não encontrado");
        }
        Curso curso = existente.get();
        curso.setNome(dto.getNome());
        cursoRepository.save(curso);
    }

    public void removerCurso(int codigo) throws DaoException {
        if (!cursoRepository.existsById(codigo)) {
            throw new DaoException("Curso com código " + codigo + " não encontrado");
        }
        cursoRepository.deleteById(codigo);
    }

    private void validar(CursoDTO dto) throws ServiceException {
        if (dto.getCodigo() < 1) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
    }

    private CursoDTO convertToDTO(Curso curso) {
        return CursoDTO.builder()
                .codigo(curso.getCodigo())
                .nome(curso.getNome())
                .build();
    }
}
