package br.edu.ibmec.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.dto.EstadoCivilDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Data;
import br.edu.ibmec.entity.EstadoCivil;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.repository.CursoRepository;

/**
 * Serviço para Aluno usando Spring Data JPA Repository
 */
@Service("alunoRepositoryService")
@Transactional
@Slf4j
public class AlunoRepositoryService {
    
    private static final int MATRICULA_MINIMA = 1;
    private static final int MATRICULA_MAXIMA = 99;
    private static final int NOME_TAMANHO_MAXIMO = 20;

    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private CursoRepository cursoRepository;

    @Transactional(readOnly = true)
    public AlunoDTO buscarAluno(int matricula) throws DaoException {
        Aluno aluno = alunoRepository.findByNumeroMatricula(matricula);
        if (aluno == null) {
            throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
        }
        
        return convertToDTO(aluno);
    }

    @Transactional(readOnly = true)
    public Collection<Aluno> listarAlunos() throws DaoException {
        return alunoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AlunoDTO> listarAlunosCompletos() throws DaoException {
        List<Aluno> alunos = alunoRepository.findAll();
        List<AlunoDTO> alunosDTO = new ArrayList<>();
        
        for (Aluno aluno : alunos) {
            alunosDTO.add(convertToDTO(aluno));
        }
        
        return alunosDTO;
    }

    @Transactional
    public void cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        validarAlunoDTO(alunoDTO);

        if (alunoRepository.existsByNumeroMatricula(alunoDTO.getMatricula())) {
            throw new ServiceException("Aluno com matrícula " + alunoDTO.getMatricula() + " já existe");
        }

        Curso curso = buscarCursoSeInformado(alunoDTO.getCurso());
        Aluno aluno = convertToEntity(alunoDTO, curso);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        validarAlunoDTO(alunoDTO);

        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoDTO.getMatricula());
        if (alunoOpt.isEmpty()) {
            throw new DaoException("Aluno com matrícula " + alunoDTO.getMatricula() + " não encontrado");
        }

        Curso curso = buscarCursoSeInformado(alunoDTO.getCurso());
        atualizarDadosAluno(alunoOpt.get(), alunoDTO, curso);
        alunoRepository.save(alunoOpt.get());
    }

    @Transactional
    public void removerAluno(int matricula) throws DaoException {
        if (!alunoRepository.existsById(matricula)) {
            throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
        }

        alunoRepository.deleteById(matricula);
    }

    // Métodos privados - Clean Code: extrair validações e lógica repetida
    private void validarAlunoDTO(AlunoDTO alunoDTO) throws ServiceException {
        if (alunoDTO.getMatricula() < MATRICULA_MINIMA || alunoDTO.getMatricula() > MATRICULA_MAXIMA) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if (alunoDTO.getNome() == null || alunoDTO.getNome().trim().isEmpty()
            || alunoDTO.getNome().trim().length() > NOME_TAMANHO_MAXIMO) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }
    }

    private Curso buscarCursoSeInformado(int codigoCurso) throws DaoException {
        if (codigoCurso <= 0) {
            return null;
        }

        Curso curso = cursoRepository.findByCodigoCurso(codigoCurso);
        if (curso == null) {
            throw new DaoException("Curso com código " + codigoCurso + " não encontrado");
        }
        return curso;
    }

    private void atualizarDadosAluno(Aluno aluno, AlunoDTO alunoDTO, Curso curso) {
        aluno.setNomeCompleto(alunoDTO.getNome().trim());
        aluno.setCursoMatriculado(curso);
        aluno.setIdadeAtual(alunoDTO.getIdade());
        aluno.setPossuiMatriculaAtiva(alunoDTO.isMatriculaAtiva());

        atualizarDataNascimento(aluno, alunoDTO.getDtNascimento());
        atualizarEstadoCivil(aluno, alunoDTO.getEstadoCivil());
        atualizarTelefones(aluno, alunoDTO.getTelefones());
    }

    private void atualizarDataNascimento(Aluno aluno, String dtNascimento) {
        if (dtNascimento != null && !dtNascimento.trim().isEmpty()) {
            try {
                Data dataNascimento = Data.fromString(dtNascimento);
                aluno.setDataNascimento(dataNascimento);
            } catch (Exception e) {
                log.warn("Erro ao converter data de nascimento '{}': {}", dtNascimento, e.getMessage());
            }
        }
    }

    private void atualizarEstadoCivil(Aluno aluno, EstadoCivilDTO estadoCivilDTO) {
        if (estadoCivilDTO != null) {
            aluno.setEstadoCivilAtual(convertEstadoCivilFromDTO(estadoCivilDTO));
        }
    }

    private void atualizarTelefones(Aluno aluno, List<String> telefones) {
        if (telefones != null) {
            List<String> telefonesNormalizados = telefones.stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .toList();
            aluno.setNumerosTelefone(new ArrayList<>(telefonesNormalizados));
        }
    }

    private AlunoDTO convertToDTO(Aluno aluno) {
        AlunoDTO dto = new AlunoDTO();
        dto.setMatricula(aluno.getNumeroMatricula());
        dto.setNome(aluno.getNomeCompleto());
        dto.setIdade(aluno.getIdadeAtual());
        dto.setMatriculaAtiva(aluno.isPossuiMatriculaAtiva());
        
        if (aluno.getCursoMatriculado() != null) {
            dto.setCurso(aluno.getCursoMatriculado().getCodigoCurso());
        }
        
        if (aluno.getDataNascimento() != null) {
            dto.setDtNascimento(aluno.getDataNascimento().toString());
        }
        
        if (aluno.getEstadoCivilAtual() != null) {
            dto.setEstadoCivil(convertEstadoCivilToDTO(aluno.getEstadoCivilAtual()));
        }
        
        if (aluno.getNumerosTelefone() != null) {
            dto.setTelefones(new ArrayList<>(aluno.getNumerosTelefone()));
        }
        
        return dto;
    }

    private Aluno convertToEntity(AlunoDTO dto, Curso curso) {
        Aluno aluno = new Aluno();
        aluno.setNumeroMatricula(dto.getMatricula());
        aluno.setNomeCompleto(dto.getNome().trim());
        aluno.setIdadeAtual(dto.getIdade());
        aluno.setPossuiMatriculaAtiva(dto.isMatriculaAtiva());
        aluno.setCursoMatriculado(curso);
        
        atualizarDataNascimento(aluno, dto.getDtNascimento());
        atualizarEstadoCivil(aluno, dto.getEstadoCivil());
        atualizarTelefones(aluno, dto.getTelefones());

        return aluno;
    }

    private EstadoCivilDTO convertEstadoCivilToDTO(EstadoCivil estadoCivil) {
        return switch (estadoCivil) {
            case solteiro -> EstadoCivilDTO.solteiro;
            case casado -> EstadoCivilDTO.casado;
            case divorciado -> EstadoCivilDTO.divorciado;
            case viuvo -> EstadoCivilDTO.viuvo;
        };
    }

    private EstadoCivil convertEstadoCivilFromDTO(EstadoCivilDTO estadoCivilDTO) {
        return switch (estadoCivilDTO) {
            case solteiro -> EstadoCivil.solteiro;
            case casado -> EstadoCivil.casado;
            case divorciado -> EstadoCivil.divorciado;
            case viuvo -> EstadoCivil.viuvo;
        };
    }
}