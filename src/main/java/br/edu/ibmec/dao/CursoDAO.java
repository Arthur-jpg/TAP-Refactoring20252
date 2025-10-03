package br.edu.ibmec.dao;

import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import java.util.Collection;
import java.util.Optional;

/**
 * Interface para operações de persistência da entidade Curso.
 * Define o contrato para acesso aos dados de cursos.
 */
public interface CursoDAO {
    
    /**
     * Salva um novo curso no banco de dados.
     * @param curso o curso a ser salvo
     * @throws DaoException se houver erro na operação
     */
    void save(Curso curso) throws DaoException;
    
    /**
     * Atualiza um curso existente no banco de dados.
     * @param curso o curso a ser atualizado
     * @return o curso atualizado
     * @throws DaoException se houver erro na operação
     */
    Curso update(Curso curso) throws DaoException;
    
    /**
     * Busca um curso pelo código.
     * @param codigo o código do curso
     * @return Optional contendo o curso se encontrado
     * @throws DaoException se houver erro na operação
     */
    Optional<Curso> findByCodigo(int codigo) throws DaoException;
    
    /**
     * Retorna todos os cursos cadastrados.
     * @return coleção de todos os cursos
     * @throws DaoException se houver erro na operação
     */
    Collection<Curso> findAll() throws DaoException;
    
    /**
     * Remove um curso do banco de dados pelo código.
     * @param codigo o código do curso a ser removido
     * @throws DaoException se houver erro na operação
     */
    void deleteByCodigo(int codigo) throws DaoException;
    
    /**
     * Verifica se existe um curso com o código especificado.
     * @param codigo o código a ser verificado
     * @return true se o curso existe, false caso contrário
     * @throws DaoException se houver erro na operação
     */
    boolean existsByCodigo(int codigo) throws DaoException;
}