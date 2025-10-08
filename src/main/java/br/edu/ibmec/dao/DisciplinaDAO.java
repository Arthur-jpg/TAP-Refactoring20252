package br.edu.ibmec.dao;

import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface para operações de persistência da entidade Disciplina.
 * Define o contrato para acesso aos dados de disciplinas.
 */
public interface DisciplinaDAO {
    
    /**
     * Salva uma nova disciplina no banco de dados.
     * @param disciplina a disciplina a ser salva
     * @throws DaoException se houver erro na operação
     */
    void save(Disciplina disciplina) throws DaoException;
    
    /**
     * Atualiza uma disciplina existente no banco de dados.
     * @param disciplina a disciplina a ser atualizada
     * @return a disciplina atualizada
     * @throws DaoException se houver erro na operação
     */
    Disciplina update(Disciplina disciplina) throws DaoException;
    
    /**
     * Busca uma disciplina pelo código.
     * @param codigo o código da disciplina
     * @return Optional contendo a disciplina se encontrada
     * @throws DaoException se houver erro na operação
     */
    Optional<Disciplina> findByCodigo(int codigo) throws DaoException;
    
    /**
     * Retorna todas as disciplinas cadastradas.
     * @return coleção de todas as disciplinas
     * @throws DaoException se houver erro na operação
     */
    Collection<Disciplina> findAll() throws DaoException;
    
    /**
     * Remove uma disciplina do banco de dados pelo código.
     * @param codigo o código da disciplina a ser removida
     * @throws DaoException se houver erro na operação
     */
    void deleteByCodigo(int codigo) throws DaoException;
    
    /**
     * Verifica se existe uma disciplina com o código especificado.
     * @param codigo o código a ser verificado
     * @return true se a disciplina existe, false caso contrário
     * @throws DaoException se houver erro na operação
     */
    boolean existsByCodigo(int codigo) throws DaoException;
    
    /**
     * Busca disciplinas por curso.
     * @param codigoCurso o código do curso
     * @return coleção de disciplinas do curso
     * @throws DaoException se houver erro na operação
     */
    Collection<Disciplina> findByCurso(int codigoCurso) throws DaoException;
}