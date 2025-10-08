package br.edu.ibmec.dao.impl;

import br.edu.ibmec.dao.DisciplinaDAO;
import br.edu.ibmec.entity.Disciplina;
import br.edu.ibmec.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementação JPA do DAO para a entidade Disciplina.
 * Utiliza EntityManager para operações de persistência.
 */
@Repository
@Transactional
public class DisciplinaDAOImpl implements DisciplinaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Disciplina disciplina) throws DaoException {
        try {
            if (existsByCodigo(disciplina.getCodigo())) {
                throw new DaoException("Disciplina com código " + disciplina.getCodigo() + " já existe");
            }
            entityManager.persist(disciplina);
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar disciplina: " + e.getMessage());
        }
    }

    @Override
    public Disciplina update(Disciplina disciplina) throws DaoException {
        try {
            if (!existsByCodigo(disciplina.getCodigo())) {
                throw new DaoException("Disciplina com código " + disciplina.getCodigo() + " não encontrada");
            }
            Disciplina updated = entityManager.merge(disciplina);
            entityManager.flush();
            return updated;
        } catch (Exception e) {
            throw new DaoException("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Disciplina> findByCodigo(int codigo) throws DaoException {
        try {
            Disciplina disciplina = entityManager.find(Disciplina.class, codigo);
            return Optional.ofNullable(disciplina);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar disciplina: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Disciplina> findAll() throws DaoException {
        try {
            TypedQuery<Disciplina> query = entityManager.createQuery(
                "SELECT d FROM Disciplina d", Disciplina.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao listar disciplinas: " + e.getMessage());
        }
    }

    @Override
    public void deleteByCodigo(int codigo) throws DaoException {
        try {
            Optional<Disciplina> disciplina = findByCodigo(codigo);
            if (disciplina.isEmpty()) {
                throw new DaoException("Disciplina com código " + codigo + " não encontrada");
            }
            
            // Verificar se há turmas associadas
            if (!disciplina.get().getTurmas().isEmpty()) {
                throw new DaoException("Não é possível remover disciplina com turmas associadas");
            }
            
            entityManager.remove(disciplina.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover disciplina: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigo(int codigo) throws DaoException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(d) FROM Disciplina d WHERE d.codigo = :codigo", Long.class);
            query.setParameter("codigo", codigo);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao verificar existência da disciplina: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Disciplina> findByCurso(int codigoCurso) throws DaoException {
        try {
            TypedQuery<Disciplina> query = entityManager.createQuery(
                "SELECT d FROM Disciplina d WHERE d.curso.codigo = :codigoCurso", Disciplina.class);
            query.setParameter("codigoCurso", codigoCurso);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar disciplinas do curso: " + e.getMessage());
        }
    }
}