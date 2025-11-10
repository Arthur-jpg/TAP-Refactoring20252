package br.edu.ibmec.service;

import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Professor;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.CursoRepository;
import br.edu.ibmec.repository.DisciplinaRepository;
import br.edu.ibmec.repository.ProfessorRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("disciplinaRepositoryService")
@Transactional
public class DisciplinaRepositoryService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public DisciplinaDTO buscarDisciplina(int codigo) throws DaoException {
        Disciplina disciplina = disciplinaRepository.findByCodigo(codigo);
        if (disciplina == null) {
            throw new DaoException("Disciplina com código " + codigo + " não encontrada");
        }
        return convertToDTO(disciplina);
    }

    @Transactional(readOnly = true)
    public List<DisciplinaDTO> listarDisciplinas() {
        return disciplinaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void cadastrarDisciplina(DisciplinaDTO dto) throws ServiceException, DaoException {
        validar(dto);
        if (disciplinaRepository.existsByCodigo(dto.getCodigo())) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_DUPLICADO);
        }
        Curso curso = obterCurso(dto.getCurso());
        Professor professor = obterProfessor(dto.getProfessorId());

        Disciplina disciplina = new Disciplina();
        disciplina.setCodigo(dto.getCodigo());
        disciplina.setNome(dto.getNome());
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
        disciplinaRepository.save(disciplina);
    }

    public void alterarDisciplina(DisciplinaDTO dto) throws ServiceException, DaoException {
        validar(dto);
        Optional<Disciplina> existente = disciplinaRepository.findById(dto.getCodigo());
        if (existente.isEmpty()) {
            throw new DaoException("Disciplina com código " + dto.getCodigo() + " não encontrada");
        }
        Disciplina disciplina = existente.get();
        disciplina.setNome(dto.getNome());
        disciplina.setCurso(obterCurso(dto.getCurso()));
        disciplina.setProfessor(obterProfessor(dto.getProfessorId()));
        disciplinaRepository.save(disciplina);
    }

    public void removerDisciplina(int codigo) throws DaoException {
        if (!disciplinaRepository.existsById(codigo)) {
            throw new DaoException("Disciplina com código " + codigo + " não encontrada");
        }
        disciplinaRepository.deleteById(codigo);
    }

    private void validar(DisciplinaDTO dto) throws ServiceException {
        if (dto.getCodigo() < 1) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if (dto.getProfessorId() == null) {
            throw new ServiceException("Professor é obrigatório");
        }
    }

    private Curso obterCurso(int codigoCurso) throws DaoException {
        Curso curso = cursoRepository.findByCodigo(codigoCurso);
        if (curso == null) {
            throw new DaoException("Curso com código " + codigoCurso + " não encontrado");
        }
        return curso;
    }

    private Professor obterProfessor(Long professorId) throws DaoException {
        return professorRepository.findById(professorId)
                .orElseThrow(() -> new DaoException("Professor com id " + professorId + " não encontrado"));
    }

    private DisciplinaDTO convertToDTO(Disciplina disciplina) {
        return DisciplinaDTO.builder()
                .codigo(disciplina.getCodigo())
                .nome(disciplina.getNome())
                .curso(disciplina.getCurso() != null ? disciplina.getCurso().getCodigo() : 0)
                .professorId(disciplina.getProfessor() != null ? disciplina.getProfessor().getId() : null)
                .build();
    }
}
