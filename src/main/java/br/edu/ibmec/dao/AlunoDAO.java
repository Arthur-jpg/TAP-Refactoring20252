package br.edu.ibmec.dao;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface para operações de persistência da entidade Aluno.
 * Define o contrato para acesso aos dados de alunos.
 */
public interface AlunoDAO {
    
    /**
     * Salva um novo aluno no banco de dados.
     * @param aluno o aluno a ser salvo
     * @throws DaoException se houver erro na operação
     */
    void save(Aluno aluno) throws DaoException;
    
    /**
     * Atualiza um aluno existente no banco de dados.
     * @param aluno o aluno a ser atualizado
     * @return o aluno atualizado
     * @throws DaoException se houver erro na operação
     */
    Aluno update(Aluno aluno) throws DaoException;
    
    /**
     * Busca um aluno pela matrícula.
     * @param matricula a matrícula do aluno
     * @return Optional contendo o aluno se encontrado
     * @throws DaoException se houver erro na operação
     */
    Optional<Aluno> findByMatricula(int matricula) throws DaoException;
    
    /**
     * Retorna todos os alunos cadastrados.
     * @return coleção de todos os alunos
     * @throws DaoException se houver erro na operação
     */
    Collection<Aluno> findAll() throws DaoException;
    
    /**
     * Remove um aluno do banco de dados pela matrícula.
     * @param matricula a matrícula do aluno a ser removido
     * @throws DaoException se houver erro na operação
     */
    void deleteByMatricula(int matricula) throws DaoException;
    
    /**
     * Verifica se existe um aluno com a matrícula especificada.
     * @param matricula a matrícula a ser verificada
     * @return true se o aluno existe, false caso contrário
     * @throws DaoException se houver erro na operação
     */
    boolean existsByMatricula(int matricula) throws DaoException;
}