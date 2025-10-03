package br.edu.ibmec.dao.impl;

import br.edu.ibmec.dao.AlunoDAO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementação JPA do DAO para a entidade Aluno.
 * Utiliza EntityManager para operações de persistência.
 */
@Repository
@Transactional
public class AlunoDAOImpl implements AlunoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Aluno aluno) throws DaoException {
        try {
            if (existsByMatricula(aluno.getMatricula())) {
                throw new DaoException("Aluno com matrícula " + aluno.getMatricula() + " já existe");
            }
            entityManager.persist(aluno);
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar aluno: " + e.getMessage());
        }
    }

    @Override
    public Aluno update(Aluno aluno) throws DaoException {
        try {
            if (!existsByMatricula(aluno.getMatricula())) {
                throw new DaoException("Aluno com matrícula " + aluno.getMatricula() + " não encontrado");
            }
            Aluno updated = entityManager.merge(aluno);
            entityManager.flush();
            return updated;
        } catch (Exception e) {
            throw new DaoException("Erro ao atualizar aluno: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aluno> findByMatricula(int matricula) throws DaoException {
        try {
            Aluno aluno = entityManager.find(Aluno.class, matricula);
            return Optional.ofNullable(aluno);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar aluno: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Aluno> findAll() throws DaoException {
        try {
            TypedQuery<Aluno> query = entityManager.createQuery(
                "SELECT a FROM Aluno a", Aluno.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao listar alunos: " + e.getMessage());
        }
    }

    @Override
    public void deleteByMatricula(int matricula) throws DaoException {
        try {
            Optional<Aluno> aluno = findByMatricula(matricula);
            if (aluno.isEmpty()) {
                throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
            }
            entityManager.remove(aluno.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover aluno: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByMatricula(int matricula) throws DaoException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(a) FROM Aluno a WHERE a.matricula = :matricula", Long.class);
            query.setParameter("matricula", matricula);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao verificar existência do aluno: " + e.getMessage());
        }
    }
}