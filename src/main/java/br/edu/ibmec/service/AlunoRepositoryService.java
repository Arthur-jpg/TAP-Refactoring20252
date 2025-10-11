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
        // Validações
        if (alunoDTO.getMatricula() < 1 || alunoDTO.getMatricula() > 99) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if (alunoDTO.getNome() == null || alunoDTO.getNome().trim().isEmpty() || alunoDTO.getNome().trim().length() > 20) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }

        // Verifica se já existe
        if (alunoRepository.existsByNumeroMatricula(alunoDTO.getMatricula())) {
            throw new ServiceException("Aluno com matrícula " + alunoDTO.getMatricula() + " já existe");
        }

        // Busca o curso se informado
        Curso curso = null;
        if (alunoDTO.getCurso() > 0) {
            curso = cursoRepository.findByCodigoCurso(alunoDTO.getCurso());
            if (curso == null) {
                throw new DaoException("Curso com código " + alunoDTO.getCurso() + " não encontrado");
            }
        }

        Aluno aluno = convertToEntity(alunoDTO, curso);
        alunoRepository.save(aluno);
    }

    @Transactional
    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        // Validações
        if (alunoDTO.getMatricula() < 1 || alunoDTO.getMatricula() > 99) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_MATRICULA_INVALIDA);
        }
        if (alunoDTO.getNome() == null || alunoDTO.getNome().trim().isEmpty() || alunoDTO.getNome().trim().length() > 20) {
            throw new ServiceException(ServiceExceptionEnum.ALUNO_NOME_INVALIDO);
        }

        // Verifica se existe
        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoDTO.getMatricula());
        if (alunoOpt.isEmpty()) {
            throw new DaoException("Aluno com matrícula " + alunoDTO.getMatricula() + " não encontrado");
        }

        // Busca o curso se informado
        Curso curso = null;
        if (alunoDTO.getCurso() > 0) {
            curso = cursoRepository.findByCodigoCurso(alunoDTO.getCurso());
            if (curso == null) {
                throw new DaoException("Curso com código " + alunoDTO.getCurso() + " não encontrado");
            }
        }

        Aluno aluno = alunoOpt.get();
        aluno.definirNomeCompleto(alunoDTO.getNome().trim());
        aluno.setCursoMatriculado(curso);
        // Atualizar demais campos
        aluno.definirIdadeAtual(alunoDTO.getIdade());
        aluno.setPossuiMatriculaAtiva(alunoDTO.isMatriculaAtiva());

        // Atualiza data de nascimento se informada
        if (alunoDTO.getDtNascimento() != null && !alunoDTO.getDtNascimento().trim().isEmpty()) {
            try {
                Data dataNascimento = Data.fromString(alunoDTO.getDtNascimento());
                aluno.setDataNascimento(dataNascimento);
            } catch (Exception e) {
                log.warn("Erro ao converter data de nascimento '{}': {}", alunoDTO.getDtNascimento(), e.getMessage());
            }
        }

        // Atualiza estado civil se informado
        if (alunoDTO.getEstadoCivil() != null) {
            aluno.setEstadoCivilAtual(convertEstadoCivilFromDTO(alunoDTO.getEstadoCivil()));
        }

        // Atualiza telefones se informado (normaliza: trim, remove vazios e duplicados)
        if (alunoDTO.getTelefones() != null) {
            List<String> telefonesNormalizados = alunoDTO.getTelefones().stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .toList();
            aluno.setNumerosTelefone(new ArrayList<>(telefonesNormalizados));
        }
        
        alunoRepository.save(aluno);
    }

    @Transactional
    public void removerAluno(int matricula) throws DaoException {
        if (!alunoRepository.existsById(matricula)) {
            throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
        }
        
        alunoRepository.deleteById(matricula);
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
        
        // Converter data de nascimento
        if (aluno.getDataNascimento() != null) {
            dto.setDtNascimento(aluno.getDataNascimento().toString());
        }
        
        // Converter estado civil
        if (aluno.getEstadoCivilAtual() != null) {
            dto.setEstadoCivil(convertEstadoCivilToDTO(aluno.getEstadoCivilAtual()));
        }
        
        // Converter telefones (cópia defensiva para evitar LazyInitialization no Jackson)
        if (aluno.getNumerosTelefone() != null) {
            dto.setTelefones(new ArrayList<>(aluno.getNumerosTelefone()));
        }
        
        return dto;
    }

    private Aluno convertToEntity(AlunoDTO dto, Curso curso) {
        Aluno aluno = new Aluno();
        aluno.definirNumeroMatricula(dto.getMatricula());
        aluno.definirNomeCompleto(dto.getNome().trim());
        aluno.definirIdadeAtual(dto.getIdade());
        aluno.setPossuiMatriculaAtiva(dto.isMatriculaAtiva());
        aluno.setCursoMatriculado(curso);
        
        // Converter data de nascimento
        if (dto.getDtNascimento() != null && !dto.getDtNascimento().trim().isEmpty()) {
            try {
                br.edu.ibmec.entity.Data dataNascimento = br.edu.ibmec.entity.Data.fromString(dto.getDtNascimento());
                aluno.setDataNascimento(dataNascimento);
            } catch (Exception e) {
                // Se houver erro na conversão, manter null e registrar aviso
                log.warn("Erro ao converter data de nascimento '{}': {}", dto.getDtNascimento(), e.getMessage());
            }
        }
        
        // Converter estado civil
        if (dto.getEstadoCivil() != null) {
            aluno.setEstadoCivilAtual(convertEstadoCivilFromDTO(dto.getEstadoCivil()));
        }
        
        // Converter telefones (normaliza: trim, remove vazios e duplicados)
        if (dto.getTelefones() != null) {
            List<String> telefonesNormalizados = dto.getTelefones().stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .toList();
            aluno.setNumerosTelefone(new ArrayList<>(telefonesNormalizados));
        }
        
        return aluno;
    }

    private EstadoCivilDTO convertEstadoCivilToDTO(EstadoCivil estadoCivil) {
        switch (estadoCivil) {
            case solteiro:
                return EstadoCivilDTO.solteiro;
            case casado:
                return EstadoCivilDTO.casado;
            case divorciado:
                return EstadoCivilDTO.divorciado;
            case viuvo:
                return EstadoCivilDTO.viuvo;
            default:
                return null;
        }
    }

    private EstadoCivil convertEstadoCivilFromDTO(EstadoCivilDTO estadoCivilDTO) {
        switch (estadoCivilDTO) {
            case solteiro:
                return EstadoCivil.solteiro;
            case casado:
                return EstadoCivil.casado;
            case divorciado:
                return EstadoCivil.divorciado;
            case viuvo:
                return EstadoCivil.viuvo;
            default:
                return null;
        }
    }
}