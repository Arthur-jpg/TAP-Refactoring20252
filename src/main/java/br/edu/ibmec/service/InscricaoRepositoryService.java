package br.edu.ibmec.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Situacao;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.repository.InscricaoRepository;
import br.edu.ibmec.repository.TurmaRepository;

/**
 * Service usando Spring Data JPA para gerenciamento de inscrições.
 * Implementa conversão Entity↔DTO e transações @Transactional.
 * 
 * @author GitHub Copilot
 * @version 2.0 - Spring Data JPA
 * @since 2024-10-09
 */
@Service
@Transactional
public class InscricaoRepositoryService {

    @Autowired
    private InscricaoRepository inscricaoRepository;
    
    @Autowired
    private AlunoRepository alunoRepository;
    
    @Autowired
    private TurmaRepository turmaRepository;

    /**
     * Lista todas as inscrições convertidas para DTO.
     * @return lista de InscricaoDTO
     * @throws DaoException se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoesCompletas() throws DaoException {
        try {
            List<Inscricao> inscricoes = inscricaoRepository.findAll();
            return inscricoes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Erro ao listar inscrições: " + e.getMessage());
        }
    }

    /**
     * Lista inscrições por matrícula do aluno convertidas para DTO.
     * @param matricula matrícula do aluno
     * @return lista de InscricaoDTO do aluno
     * @throws DaoException se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoesPorAluno(int matricula) throws DaoException {
        try {
            List<Inscricao> inscricoes = inscricaoRepository.findByAlunoNumeroMatricula(matricula);
            return inscricoes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Erro ao listar inscrições do aluno " + matricula + ": " + e.getMessage());
        }
    }

    /**
     * Lista inscrições por código da turma convertidas para DTO.
     * @param codigoTurma código da turma
     * @return lista de InscricaoDTO da turma
     * @throws DaoException se houver erro na consulta
     */
    @Transactional(readOnly = true)
    public List<InscricaoDTO> listarInscricoesPorTurma(int codigoTurma) throws DaoException {
        try {
            List<Inscricao> inscricoes = inscricaoRepository.findByTurmaCodigo(codigoTurma);
            return inscricoes.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Erro ao listar inscrições da turma " + codigoTurma + ": " + e.getMessage());
        }
    }

    /**
     * Busca inscrição específica por matrícula, código, ano e semestre.
     * @param matricula matrícula do aluno
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @return InscricaoDTO encontrada
     * @throws DaoException se inscrição não for encontrada
     */
    @Transactional(readOnly = true)
    public InscricaoDTO buscarInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        try {
            List<Inscricao> todasInscricoes = inscricaoRepository.findAll();
            Optional<Inscricao> inscricaoOpt = todasInscricoes.stream()
                    .filter(inscricao -> inscricao.getAluno() != null && 
                            inscricao.getAluno().getNumeroMatricula() == matricula &&
                            inscricao.getTurma() != null &&
                            inscricao.getTurma().getCodigo() == codigo &&
                            inscricao.getTurma().getAno() == ano &&
                            inscricao.getTurma().getSemestre() == semestre)
                    .findFirst();
            
            if (inscricaoOpt.isEmpty()) {
                throw new DaoException("Inscrição não encontrada para matrícula " + matricula + 
                        " na turma " + codigo + "/" + ano + "/" + semestre);
            }
            
            return convertToDTO(inscricaoOpt.get());
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar inscrição: " + e.getMessage());
        }
    }

    /**
     * Cadastra nova inscrição.
     * @param inscricaoDTO dados da inscrição
     * @throws DaoException se houver erro na operação
     * @throws ServiceException se dados inválidos
     */
    public void cadastrarInscricao(InscricaoDTO inscricaoDTO) throws DaoException, ServiceException {
        try {
            if (inscricaoDTO.getAluno() <= 0) {
                throw new ServiceException("Matrícula do aluno é obrigatória");
            }
            
            if (inscricaoDTO.getCodigo() < 1 || inscricaoDTO.getCodigo() > 999) {
                throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
            }
            if (inscricaoDTO.getAno() < 1900 || inscricaoDTO.getAno() > 2020) {
                throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
            }
            Optional<Aluno> alunoOpt = alunoRepository.findById(inscricaoDTO.getAluno());
            if (alunoOpt.isEmpty()) {
                throw new ServiceException("Aluno não encontrado com matrícula " + inscricaoDTO.getAluno());
            }
            List<Turma> todasTurmas = turmaRepository.findAll();
            Optional<Turma> turmaOpt = todasTurmas.stream()
                    .filter(turma -> turma.getCodigo() == inscricaoDTO.getCodigo() &&
                            turma.getAno() == inscricaoDTO.getAno() &&
                            turma.getSemestre() == inscricaoDTO.getSemestre())
                    .findFirst();
            
            if (turmaOpt.isEmpty()) {
                throw new ServiceException("Turma não encontrada com código " + inscricaoDTO.getCodigo() + 
                        "/" + inscricaoDTO.getAno() + "/" + inscricaoDTO.getSemestre());
            }

            List<Inscricao> inscricoesExistentes = inscricaoRepository.findAll();
            boolean jaExiste = inscricoesExistentes.stream()
                    .anyMatch(inscricao -> inscricao.getAluno() != null && 
                            inscricao.getAluno().getNumeroMatricula() == inscricaoDTO.getAluno() &&
                            inscricao.getTurma() != null &&
                            inscricao.getTurma().getCodigo() == inscricaoDTO.getCodigo() &&
                            inscricao.getTurma().getAno() == inscricaoDTO.getAno() &&
                            inscricao.getTurma().getSemestre() == inscricaoDTO.getSemestre());
            
            if (jaExiste) {
                throw new ServiceException("Aluno já inscrito nesta turma");
            }

            Inscricao novaInscricao = convertToEntity(inscricaoDTO, alunoOpt.get(), turmaOpt.get());
            inscricaoRepository.save(novaInscricao);
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Erro ao cadastrar inscrição: " + e.getMessage());
        }
    }

    /**
     * Altera inscrição existente.
     * @param inscricaoDTO dados da inscrição
     * @throws DaoException se houver erro na operação
     * @throws ServiceException se dados inválidos
     */
    public void alterarInscricao(InscricaoDTO inscricaoDTO) throws DaoException, ServiceException {
        try {
            List<Inscricao> todasInscricoes = inscricaoRepository.findAll();
            Optional<Inscricao> inscricaoOpt = todasInscricoes.stream()
                    .filter(inscricao -> inscricao.getAluno() != null && 
                            inscricao.getAluno().getNumeroMatricula() == inscricaoDTO.getAluno() &&
                            inscricao.getTurma() != null &&
                            inscricao.getTurma().getCodigo() == inscricaoDTO.getCodigo() &&
                            inscricao.getTurma().getAno() == inscricaoDTO.getAno() &&
                            inscricao.getTurma().getSemestre() == inscricaoDTO.getSemestre())
                    .findFirst();
            
            if (inscricaoOpt.isEmpty()) {
                throw new ServiceException("Inscrição não encontrada");
            }

            Inscricao inscricao = inscricaoOpt.get();
            
            inscricao.setAvaliacao1(inscricaoDTO.getAvaliacao1());
            inscricao.setAvaliacao2(inscricaoDTO.getAvaliacao2());
            inscricao.setNumFaltas(inscricaoDTO.getNumFaltas());
            
            if (inscricaoDTO.getSituacao() != null && !inscricaoDTO.getSituacao().trim().isEmpty()) {
                try {
                    inscricao.setSituacao(Situacao.valueOf(inscricaoDTO.getSituacao().toLowerCase()));
                } catch (IllegalArgumentException e) {
                    throw new ServiceException("Situação inválida: " + inscricaoDTO.getSituacao());
                }
            }

            inscricaoRepository.save(inscricao);
            
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Erro ao alterar inscrição: " + e.getMessage());
        }
    }

    /**
     * Remove inscrição por matrícula, código, ano e semestre.
     * @param matricula matrícula do aluno
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @throws DaoException se houver erro na operação
     */
    public void removerInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        try {
            List<Inscricao> todasInscricoes = inscricaoRepository.findAll();
            Optional<Inscricao> inscricaoOpt = todasInscricoes.stream()
                    .filter(inscricao -> inscricao.getAluno() != null && 
                            inscricao.getAluno().getNumeroMatricula() == matricula &&
                            inscricao.getTurma() != null &&
                            inscricao.getTurma().getCodigo() == codigo &&
                            inscricao.getTurma().getAno() == ano &&
                            inscricao.getTurma().getSemestre() == semestre)
                    .findFirst();
            
            if (inscricaoOpt.isEmpty()) {
                throw new DaoException("Inscrição não encontrada");
            }

            inscricaoRepository.delete(inscricaoOpt.get());
            
        } catch (Exception e) {
            throw new DaoException("Erro ao remover inscrição: " + e.getMessage());
        }
    }

    /**
     * Converte entidade Inscricao para DTO.
     * @param inscricao entidade
     * @return InscricaoDTO
     */
    private InscricaoDTO convertToDTO(Inscricao inscricao) {
    return InscricaoDTO.builder()
        .avaliacao1(inscricao.getAvaliacao1())
        .avaliacao2(inscricao.getAvaliacao2())
        .media(0f) // media não era definida antes; mantendo 0 como padrão
        .numFaltas(inscricao.getNumFaltas())
        .situacao(inscricao.getSituacao() != null ? inscricao.getSituacao().name() : "ATIVA")
        .aluno(inscricao.getAluno() != null ? inscricao.getAluno().getNumeroMatricula() : 0)
        .codigo(inscricao.getTurma() != null ? inscricao.getTurma().getCodigo() : 0)
        .ano(inscricao.getTurma() != null ? inscricao.getTurma().getAno() : 0)
        .semestre(inscricao.getTurma() != null ? inscricao.getTurma().getSemestre() : 0)
        .build();
    }

    /**
     * Converte DTO para entidade Inscricao.
     * @param dto DTO
     * @param aluno entidade Aluno
     * @param turma entidade Turma
     * @return entidade Inscricao
     */
    private Inscricao convertToEntity(InscricaoDTO dto, Aluno aluno, Turma turma) {
        Inscricao inscricao = new Inscricao();
        inscricao.setAvaliacao1(dto.getAvaliacao1());
        inscricao.setAvaliacao2(dto.getAvaliacao2());
        inscricao.setNumFaltas(dto.getNumFaltas());
        
        if (dto.getSituacao() != null && !dto.getSituacao().trim().isEmpty()) {
            try {
                inscricao.setSituacao(Situacao.valueOf(dto.getSituacao().toLowerCase()));
            } catch (IllegalArgumentException e) {
                inscricao.setSituacao(Situacao.aprovado); // Default
            }
        } else {
            inscricao.setSituacao(Situacao.aprovado); // Default
        }
        
        inscricao.setAluno(aluno);
        inscricao.setTurma(turma);
        
        return inscricao;
    }
}