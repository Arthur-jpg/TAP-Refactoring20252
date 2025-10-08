package br.edu.ibmec.dao.impl;

import br.edu.ibmec.dao.CursoDAO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementação JPA do DAO para a entidade Curso.
 * Utiliza EntityManager para operações de persistência.
 */
@Repository
public class CursoDAOImpl implements CursoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Curso curso) throws DaoException {
        try {
            if (existsByCodigo(curso.getCodigo())) {
                throw new DaoException("Curso com código " + curso.getCodigo() + " já existe");
            }
            entityManager.persist(curso);
            // Removido entityManager.flush() para evitar conflitos de transação
        } catch (DaoException e) {
            throw e; // Re-throw DaoException sem encapsular
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar curso: " + e.getMessage());
        }
    }

    @Override
    public Curso update(Curso curso) throws DaoException {
        try {
            if (!existsByCodigo(curso.getCodigo())) {
                throw new DaoException("Curso com código " + curso.getCodigo() + " não encontrado");
            }
            Curso updated = entityManager.merge(curso);
            entityManager.flush();
            return updated;
        } catch (Exception e) {
            throw new DaoException("Erro ao atualizar curso: " + e.getMessage());
        }
    }

    @Override
    public Optional<Curso> findByCodigo(int codigo) throws DaoException {
        try {
            Curso curso = entityManager.find(Curso.class, codigo);
            return Optional.ofNullable(curso);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar curso: " + e.getMessage());
        }
    }

    @Override
    public Collection<Curso> findAll() throws DaoException {
        try {
            TypedQuery<Curso> query = entityManager.createQuery(
                "SELECT c FROM Curso c", Curso.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao listar cursos: " + e.getMessage());
        }
    }

    @Override
    public void deleteByCodigo(int codigo) throws DaoException {
        try {
            Optional<Curso> curso = findByCodigo(codigo);
            if (curso.isEmpty()) {
                throw new DaoException("Curso com código " + codigo + " não encontrado");
            }
            
            // Verificar se há alunos matriculados
            if (!curso.get().getAlunos().isEmpty()) {
                throw new DaoException("Não é possível remover curso com alunos matriculados");
            }
            
            entityManager.remove(curso.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover curso: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByCodigo(int codigo) throws DaoException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Curso c WHERE c.codigo = :codigo", Long.class);
            query.setParameter("codigo", codigo);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao verificar existência do curso: " + e.getMessage());
        }
    }
}