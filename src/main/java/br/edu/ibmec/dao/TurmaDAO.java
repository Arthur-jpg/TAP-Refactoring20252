package br.edu.ibmec.dao;

import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.TurmaId;
import br.edu.ibmec.exception.DaoException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface para operações de persistência da entidade Turma.
 * Define o contrato para acesso aos dados de turmas.
 */
public interface TurmaDAO {
    
    /**
     * Salva uma nova turma no banco de dados.
     * @param turma a turma a ser salva
     * @throws DaoException se houver erro na operação
     */
    void save(Turma turma) throws DaoException;
    
    /**
     * Atualiza uma turma existente no banco de dados.
     * @param turma a turma a ser atualizada
     * @return a turma atualizada
     * @throws DaoException se houver erro na operação
     */
    Turma update(Turma turma) throws DaoException;
    
    /**
     * Busca uma turma pela chave composta (código, ano, semestre).
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @return Optional contendo a turma se encontrada
     * @throws DaoException se houver erro na operação
     */
    Optional<Turma> findByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException;
    
    /**
     * Retorna todas as turmas cadastradas.
     * @return coleção de todas as turmas
     * @throws DaoException se houver erro na operação
     */
    Collection<Turma> findAll() throws DaoException;
    
    /**
     * Remove uma turma do banco de dados pela chave composta.
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @throws DaoException se houver erro na operação
     */
    void deleteByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException;
    
    /**
     * Verifica se existe uma turma com a chave especificada.
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @return true se a turma existe, false caso contrário
     * @throws DaoException se houver erro na operação
     */
    boolean existsByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException;
    
    /**
     * Busca turmas por disciplina.
     * @param codigoDisciplina o código da disciplina
     * @return coleção de turmas da disciplina
     * @throws DaoException se houver erro na operação
     */
    Collection<Turma> findByDisciplina(int codigoDisciplina) throws DaoException;
    
    /**
     * Busca turmas por ano e semestre.
     * @param ano ano das turmas
     * @param semestre semestre das turmas
     * @return coleção de turmas do período
     * @throws DaoException se houver erro na operação
     */
    Collection<Turma> findByAnoSemestre(int ano, int semestre) throws DaoException;
}