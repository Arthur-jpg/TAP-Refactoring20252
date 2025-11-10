package br.edu.ibmec.service;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.AlunoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("alunoRepositoryService")
@Transactional
public class AlunoRepositoryService {

    private static final int MATRICULA_MINIMA = 1;

    @Autowired
    private AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public AlunoDTO buscarAluno(int matricula) throws DaoException {
        Aluno aluno = alunoRepository.findByMatricula(matricula);
        if (aluno == null) {
            throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
        }
        return convertToDTO(aluno);
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> listarAlunos() {
        return alunoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException {
        validar(alunoDTO);
        if (alunoRepository.existsByMatricula(alunoDTO.getMatricula())) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        alunoRepository.save(convertToEntity(alunoDTO));
    }

    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        validar(alunoDTO);
        Optional<Aluno> existente = alunoRepository.findById(alunoDTO.getMatricula());
        if (existente.isEmpty()) {
            throw new DaoException("Aluno com matrícula " + alunoDTO.getMatricula() + " não encontrado");
        }
        Aluno aluno = existente.get();
        aluno.setNome(alunoDTO.getNome());
        alunoRepository.save(aluno);
    }

    public void removerAluno(int matricula) throws DaoException {
        if (!alunoRepository.existsById(matricula)) {
            throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
        }
        alunoRepository.deleteById(matricula);
    }

    private void validar(AlunoDTO dto) throws ServiceException {
        if (dto.getMatricula() < MATRICULA_MINIMA) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }
    }

    private AlunoDTO convertToDTO(Aluno aluno) {
        return AlunoDTO.builder()
                .matricula(aluno.getMatricula())
                .nome(aluno.getNome())
                .build();
    }

    private Aluno convertToEntity(AlunoDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setMatricula(dto.getMatricula());
        aluno.setNome(dto.getNome());
        return aluno;
    }
}
