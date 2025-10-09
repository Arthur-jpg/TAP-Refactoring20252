package br.edu.ibmec.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.TurmaId;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.DisciplinaRepository;
import br.edu.ibmec.repository.TurmaRepository;

/**
 * Service usando Spring Data JPA para gerenciamento de turmas.
 * Implementa conversão Entity↔DTO e transações @Transactional.
 * 
 * @author GitHub Copilot
 * @version 2.0 - Spring Data JPA
 * @since 2024-10-09
 */
@Service
@Transactional
public class TurmaRepositoryService {

    @Autowired
    private TurmaRepository turmaRepository;
    
    @Autowired
    private DisciplinaRepository disciplinaRepository;

    /**
     * Lista todas as turmas convertidas para DTO.
     * @return lista de TurmaDTO
     * @throws DaoException se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<TurmaDTO> listarTurmasCompletas() throws DaoException {
        try {
            List<Turma> turmas = turmaRepository.findAll();
            return turmas.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Erro ao listar turmas: " + e.getMessage());
        }
    }

    /**
     * Lista turmas por código da disciplina convertidas para DTO.
     * @param codigoDisciplina código da disciplina
     * @return lista de TurmaDTO da disciplina
     * @throws DaoException se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<TurmaDTO> listarTurmasPorDisciplina(int codigoDisciplina) throws DaoException {
        try {
            List<Turma> todasTurmas = turmaRepository.findAll();
            return todasTurmas.stream()
                    .filter(turma -> turma.getDisciplina() != null && 
                            turma.getDisciplina().getCodigo() == codigoDisciplina)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Erro ao listar turmas da disciplina " + codigoDisciplina + ": " + e.getMessage());
        }
    }

    /**
     * Busca turma específica por código, ano e semestre.
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @return TurmaDTO encontrada
     * @throws DaoException se turma não for encontrada
     */
    @Transactional(readOnly = true)
    public TurmaDTO buscarTurma(int codigo, int ano, int semestre) throws DaoException {
        try {
            Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(codigo, ano, semestre);
            if (turma == null) {
                throw new DaoException("Turma não encontrada com código " + codigo + 
                        "/" + ano + "/" + semestre);
            }
            return convertToDTO(turma);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar turma: " + e.getMessage());
        }
    }

    /**
     * Cadastra nova turma.
     * @param turmaDTO dados da turma
     * @throws DaoException se houver erro na operação
     * @throws ServiceException se dados inválidos
     */
    public void cadastrarTurma(TurmaDTO turmaDTO) throws DaoException, ServiceException {
        try {
            // Validações básicas
            if (turmaDTO.getCodigo() <= 0) {
                throw new ServiceException("Código da turma é obrigatório");
            }
            
            if (turmaDTO.getAno() <= 0) {
                throw new ServiceException("Ano da turma é obrigatório");
            }
            
            if (turmaDTO.getSemestre() < 1 || turmaDTO.getSemestre() > 2) {
                throw new ServiceException("Semestre deve ser 1 ou 2");
            }

            if (turmaDTO.getDisciplina() <= 0) {
                throw new ServiceException("Código da disciplina é obrigatório");
            }

            // Busca a disciplina
            Optional<Disciplina> disciplinaOpt = disciplinaRepository.findById(turmaDTO.getDisciplina());
            if (disciplinaOpt.isEmpty()) {
                throw new ServiceException("Disciplina não encontrada com código " + turmaDTO.getDisciplina());
            }

            // Verifica se já existe turma
            Turma turmaExistente = turmaRepository.findByCodigoAndAnoAndSemestre(
                    turmaDTO.getCodigo(), turmaDTO.getAno(), turmaDTO.getSemestre());
            
            if (turmaExistente != null) {
                throw new ServiceException("Turma já existe com código " + turmaDTO.getCodigo() + 
                        "/" + turmaDTO.getAno() + "/" + turmaDTO.getSemestre());
            }

            // Cria nova turma
            Turma novaTurma = convertToEntity(turmaDTO, disciplinaOpt.get());
            turmaRepository.save(novaTurma);
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Erro ao cadastrar turma: " + e.getMessage());
        }
    }

    /**
     * Altera turma existente.
     * @param turmaDTO dados da turma
     * @throws DaoException se houver erro na operação
     * @throws ServiceException se dados inválidos
     */
    public void alterarTurma(TurmaDTO turmaDTO) throws DaoException, ServiceException {
        try {
            // Busca turma existente
            Turma turmaExistente = turmaRepository.findByCodigoAndAnoAndSemestre(
                    turmaDTO.getCodigo(), turmaDTO.getAno(), turmaDTO.getSemestre());
            
            if (turmaExistente == null) {
                throw new ServiceException("Turma não encontrada");
            }

            // Busca a disciplina se foi alterada
            if (turmaDTO.getDisciplina() > 0) {
                Optional<Disciplina> disciplinaOpt = disciplinaRepository.findById(turmaDTO.getDisciplina());
                if (disciplinaOpt.isPresent()) {
                    turmaExistente.setDisciplina(disciplinaOpt.get());
                }
            }

            turmaRepository.save(turmaExistente);
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Erro ao alterar turma: " + e.getMessage());
        }
    }

    /**
     * Remove turma por código, ano e semestre.
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @throws DaoException se houver erro na operação
     */
    public void removerTurma(int codigo, int ano, int semestre) throws DaoException {
        try {
            Turma turma = turmaRepository.findByCodigoAndAnoAndSemestre(codigo, ano, semestre);
            
            if (turma == null) {
                throw new DaoException("Turma não encontrada");
            }

            turmaRepository.delete(turma);
            
        } catch (Exception e) {
            throw new DaoException("Erro ao remover turma: " + e.getMessage());
        }
    }

    /**
     * Converte entidade Turma para DTO.
     * @param turma entidade
     * @return TurmaDTO
     */
    private TurmaDTO convertToDTO(Turma turma) {
        return new TurmaDTO(
                turma.getCodigo(),
                turma.getAno(),
                turma.getSemestre(),
                turma.getDisciplina() != null ? turma.getDisciplina().getCodigo() : 0
        );
    }

    /**
     * Converte DTO para entidade Turma.
     * @param dto DTO
     * @param disciplina entidade Disciplina
     * @return entidade Turma
     */
    private Turma convertToEntity(TurmaDTO dto, Disciplina disciplina) {
        Turma turma = new Turma();
        
        // A entidade Turma usa @IdClass, então configuramos os campos diretamente
        turma.setCodigo(dto.getCodigo());
        turma.setAno(dto.getAno());
        turma.setSemestre(dto.getSemestre());
        turma.setDisciplina(disciplina);
        
        return turma;
    }
}