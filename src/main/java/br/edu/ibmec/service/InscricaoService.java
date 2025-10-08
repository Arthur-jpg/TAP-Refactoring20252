package br.edu.ibmec.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.dao.InscricaoDAO;
import br.edu.ibmec.dao.AlunoDAO;
import br.edu.ibmec.dao.TurmaDAO;
import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.Situacao;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InscricaoService {

    @Autowired
    private InscricaoDAO inscricaoDAO;
    
    @Autowired
    private AlunoDAO alunoDAO;
    
    @Autowired
    private TurmaDAO turmaDAO;

    public InscricaoDTO buscarInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        try {
            Optional<Inscricao> inscricaoOpt = inscricaoDAO.findByAlunoAndTurma(matricula, codigo, ano, semestre);
            if (inscricaoOpt.isEmpty()) {
                throw new DaoException("Inscrição não encontrada para matrícula " + matricula + " na turma " + codigo + "/" + ano + "/" + semestre);
            }
            
            Inscricao inscricao = inscricaoOpt.get();
            InscricaoDTO inscricaoDTO = new InscricaoDTO(
                    inscricao.getAvaliacao1(), 
                    inscricao.getAvaliacao2(),
                    inscricao.getNumFaltas(), 
                    inscricao.getSituacao() != null ? inscricao.getSituacao().name() : "ATIVA", 
                    inscricao.getAluno() != null ? inscricao.getAluno().getMatricula() : 0, 
                    inscricao.getTurma() != null ? inscricao.getTurma().getCodigo() : 0,
                    inscricao.getTurma() != null ? inscricao.getTurma().getAno() : 0, 
                    inscricao.getTurma() != null ? inscricao.getTurma().getSemestre() : 0);
            return inscricaoDTO;
        } catch (DaoException e) {
            throw new DaoException("Erro ao buscar inscrição: " + e.getMessage());
        }
    }

    public Collection<Inscricao> listarInscricoes() throws DaoException {
        return inscricaoDAO.findAll();
    }

    public void cadastrarInscricao(InscricaoDTO inscricaoDTO) throws ServiceException, DaoException {
        // Validações
        if ((inscricaoDTO.getCodigo() < 1) || (inscricaoDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((inscricaoDTO.getAno() < 1900) || (inscricaoDTO.getAno() > 2100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if ((inscricaoDTO.getSemestre() < 1) || (inscricaoDTO.getSemestre() > 2)) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }

        // Verificar se o aluno existe
        Optional<Aluno> alunoOpt = alunoDAO.findByMatricula(inscricaoDTO.getAluno());
        if (alunoOpt.isEmpty()) {
            throw new ServiceException("Aluno não encontrado");
        }

        // Verificar se a turma existe
        Optional<Turma> turmaOpt = turmaDAO.findByCodigoAnoSemestre(
                inscricaoDTO.getCodigo(), inscricaoDTO.getAno(), inscricaoDTO.getSemestre());
        if (turmaOpt.isEmpty()) {
            throw new ServiceException("Turma não encontrada");
        }

        Inscricao inscricao = new Inscricao(
                inscricaoDTO.getAvaliacao1(),
                inscricaoDTO.getAvaliacao2(), 
                inscricaoDTO.getNumFaltas(),
                Situacao.valueOf(inscricaoDTO.getSituacao()), 
                alunoOpt.get(), 
                turmaOpt.get());

        try {
            inscricaoDAO.save(inscricao);
        } catch (DaoException e) {
            throw new DaoException("Erro ao cadastrar inscrição: " + e.getMessage());
        }
    }

    public void alterarInscricao(InscricaoDTO inscricaoDTO) throws ServiceException, DaoException {
        // Validações
        if ((inscricaoDTO.getCodigo() < 1) || (inscricaoDTO.getCodigo() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((inscricaoDTO.getAno() < 1900) || (inscricaoDTO.getAno() > 2100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        if ((inscricaoDTO.getSemestre() < 1) || (inscricaoDTO.getSemestre() > 2)) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }

        // Verificar se o aluno existe
        Optional<Aluno> alunoOpt = alunoDAO.findByMatricula(inscricaoDTO.getAluno());
        if (alunoOpt.isEmpty()) {
            throw new ServiceException("Aluno não encontrado");
        }

        // Verificar se a turma existe
        Optional<Turma> turmaOpt = turmaDAO.findByCodigoAnoSemestre(
                inscricaoDTO.getCodigo(), inscricaoDTO.getAno(), inscricaoDTO.getSemestre());
        if (turmaOpt.isEmpty()) {
            throw new ServiceException("Turma não encontrada");
        }

        Inscricao inscricao = new Inscricao(
                inscricaoDTO.getAvaliacao1(),
                inscricaoDTO.getAvaliacao2(), 
                inscricaoDTO.getNumFaltas(),
                Situacao.valueOf(inscricaoDTO.getSituacao()), 
                alunoOpt.get(), 
                turmaOpt.get());

        try {
            inscricaoDAO.update(inscricao);
        } catch (DaoException e) {
            throw new DaoException("Erro ao alterar inscrição: " + e.getMessage());
        }
    }

    public void removerInscricao(int matricula, int codigo, int ano, int semestre) throws DaoException {
        try {
            inscricaoDAO.deleteByAlunoAndTurma(matricula, codigo, ano, semestre);
        } catch (DaoException e) {
            throw new DaoException("Erro ao remover inscrição: " + e.getMessage());
        }
    }

}