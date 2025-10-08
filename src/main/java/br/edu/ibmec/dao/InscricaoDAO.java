package br.edu.ibmec.dao;

import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface para operações de persistência da entidade Inscricao.
 * Define o contrato para acesso aos dados de inscrições.
 */
public interface InscricaoDAO {
    
    /**
     * Salva uma nova inscrição no banco de dados.
     * @param inscricao a inscrição a ser salva
     * @throws DaoException se houver erro na operação
     */
    void save(Inscricao inscricao) throws DaoException;
    
    /**
     * Atualiza uma inscrição existente no banco de dados.
     * @param inscricao a inscrição a ser atualizada
     * @return a inscrição atualizada
     * @throws DaoException se houver erro na operação
     */
    Inscricao update(Inscricao inscricao) throws DaoException;
    
    /**
     * Busca uma inscrição pelo ID.
     * @param id o ID da inscrição
     * @return Optional contendo a inscrição se encontrada
     * @throws DaoException se houver erro na operação
     */
    Optional<Inscricao> findById(Long id) throws DaoException;
    
    /**
     * Busca inscrição por aluno e turma.
     * @param matriculaAluno matrícula do aluno
     * @param codigoTurma código da turma
     * @param anoTurma ano da turma
     * @param semestreTurma semestre da turma
     * @return Optional contendo a inscrição se encontrada
     * @throws DaoException se houver erro na operação
     */
    Optional<Inscricao> findByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                                          int anoTurma, int semestreTurma) throws DaoException;
    
    /**
     * Retorna todas as inscrições cadastradas.
     * @return coleção de todas as inscrições
     * @throws DaoException se houver erro na operação
     */
    Collection<Inscricao> findAll() throws DaoException;
    
    /**
     * Remove uma inscrição do banco de dados pelo ID.
     * @param id o ID da inscrição a ser removida
     * @throws DaoException se houver erro na operação
     */
    void deleteById(Long id) throws DaoException;
    
    /**
     * Busca inscrições por aluno.
     * @param matriculaAluno a matrícula do aluno
     * @return coleção de inscrições do aluno
     * @throws DaoException se houver erro na operação
     */
    Collection<Inscricao> findByAluno(int matriculaAluno) throws DaoException;
    
    /**
     * Busca inscrições por turma.
     * @param codigoTurma código da turma
     * @param anoTurma ano da turma
     * @param semestreTurma semestre da turma
     * @return coleção de inscrições da turma
     * @throws DaoException se houver erro na operação
     */
    Collection<Inscricao> findByTurma(int codigoTurma, int anoTurma, int semestreTurma) throws DaoException;
    
    /**
     * Verifica se existe inscrição para aluno e turma.
     * @param matriculaAluno matrícula do aluno
     * @param codigoTurma código da turma
     * @param anoTurma ano da turma
     * @param semestreTurma semestre da turma
     * @return true se a inscrição existe, false caso contrário
     * @throws DaoException se houver erro na operação
     */
    boolean existsByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                                int anoTurma, int semestreTurma) throws DaoException;
    
    /**
     * Remove inscrição por aluno e turma.
     * @param matriculaAluno matrícula do aluno
     * @param codigoTurma código da turma
     * @param anoTurma ano da turma
     * @param semestreTurma semestre da turma
     * @throws DaoException se houver erro na operação
     */
    void deleteByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                             int anoTurma, int semestreTurma) throws DaoException;
}