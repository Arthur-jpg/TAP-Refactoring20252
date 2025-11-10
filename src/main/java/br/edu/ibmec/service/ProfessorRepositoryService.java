package br.edu.ibmec.service;

import br.edu.ibmec.dto.ProfessorDTO;
import br.edu.ibmec.entity.Professor;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.ProfessorRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProfessorRepositoryService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional(readOnly = true)
    public List<ProfessorDTO> listarProfessores() {
        return professorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessorDTO buscarProfessor(Long id) throws DaoException {
        Optional<Professor> professor = professorRepository.findById(id);
        if (professor.isEmpty()) {
            throw new DaoException("Professor com id " + id + " não encontrado");
        }
        return convertToDTO(professor.get());
    }

    public ProfessorDTO cadastrarProfessor(ProfessorDTO dto) throws ServiceException {
        validar(dto);
        if (professorRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new ServiceException("Professor já cadastrado com este nome");
        }
        Professor professor = new Professor();
        professor.setNome(dto.getNome());
        Professor salvo = professorRepository.save(professor);
        return convertToDTO(salvo);
    }

    public void alterarProfessor(ProfessorDTO dto) throws DaoException, ServiceException {
        if (dto.getId() == null) {
            throw new ServiceException("Id do professor é obrigatório para alteração");
        }
        validar(dto);
        Professor professor = professorRepository.findById(dto.getId())
                .orElseThrow(() -> new DaoException("Professor com id " + dto.getId() + " não encontrado"));
        professor.setNome(dto.getNome());
        professorRepository.save(professor);
    }

    public void removerProfessor(Long id) throws DaoException {
        if (!professorRepository.existsById(id)) {
            throw new DaoException("Professor com id " + id + " não encontrado");
        }
        professorRepository.deleteById(id);
    }

    private void validar(ProfessorDTO dto) throws ServiceException {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new ServiceException("Nome do professor é obrigatório");
        }
    }

    private ProfessorDTO convertToDTO(Professor professor) {
        return ProfessorDTO.builder()
                .id(professor.getId())
                .nome(professor.getNome())
                .build();
    }
}
