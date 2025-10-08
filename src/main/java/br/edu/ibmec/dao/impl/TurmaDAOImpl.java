package br.edu.ibmec.dao.impl;

import br.edu.ibmec.dao.TurmaDAO;
import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.TurmaId;
import br.edu.ibmec.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementação JPA do DAO para a entidade Turma.
 * Utiliza EntityManager para operações de persistência.
 */
@Repository
@Transactional
public class TurmaDAOImpl implements TurmaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Turma turma) throws DaoException {
        try {
            if (existsByCodigoAnoSemestre(turma.getCodigo(), turma.getAno(), turma.getSemestre())) {
                throw new DaoException("Turma " + turma.getCodigo() + "/" + 
                    turma.getAno() + "/" + turma.getSemestre() + " já existe");
            }
            entityManager.persist(turma);
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar turma: " + e.getMessage());
        }
    }

    @Override
    public Turma update(Turma turma) throws DaoException {
        try {
            if (!existsByCodigoAnoSemestre(turma.getCodigo(), turma.getAno(), turma.getSemestre())) {
                throw new DaoException("Turma " + turma.getCodigo() + "/" + 
                    turma.getAno() + "/" + turma.getSemestre() + " não encontrada");
            }
            Turma updated = entityManager.merge(turma);
            entityManager.flush();
            return updated;
        } catch (Exception e) {
            throw new DaoException("Erro ao atualizar turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turma> findByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException {
        try {
            TurmaId id = new TurmaId(codigo, ano, semestre);
            Turma turma = entityManager.find(Turma.class, id);
            return Optional.ofNullable(turma);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Turma> findAll() throws DaoException {
        try {
            TypedQuery<Turma> query = entityManager.createQuery(
                "SELECT t FROM Turma t", Turma.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao listar turmas: " + e.getMessage());
        }
    }

    @Override
    public void deleteByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException {
        try {
            Optional<Turma> turma = findByCodigoAnoSemestre(codigo, ano, semestre);
            if (turma.isEmpty()) {
                throw new DaoException("Turma " + codigo + "/" + ano + "/" + semestre + " não encontrada");
            }
            
            // Verificar se há inscrições associadas
            if (!turma.get().getInscricoes().isEmpty()) {
                throw new DaoException("Não é possível remover turma com inscrições associadas");
            }
            
            entityManager.remove(turma.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCodigoAnoSemestre(int codigo, int ano, int semestre) throws DaoException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(t) FROM Turma t WHERE t.codigo = :codigo AND t.ano = :ano AND t.semestre = :semestre", 
                Long.class);
            query.setParameter("codigo", codigo);
            query.setParameter("ano", ano);
            query.setParameter("semestre", semestre);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao verificar existência da turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Turma> findByDisciplina(int codigoDisciplina) throws DaoException {
        try {
            TypedQuery<Turma> query = entityManager.createQuery(
                "SELECT t FROM Turma t WHERE t.disciplina.codigo = :codigoDisciplina", Turma.class);
            query.setParameter("codigoDisciplina", codigoDisciplina);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar turmas da disciplina: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Turma> findByAnoSemestre(int ano, int semestre) throws DaoException {
        try {
            TypedQuery<Turma> query = entityManager.createQuery(
                "SELECT t FROM Turma t WHERE t.ano = :ano AND t.semestre = :semestre", Turma.class);
            query.setParameter("ano", ano);
            query.setParameter("semestre", semestre);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar turmas do período: " + e.getMessage());
        }
    }
}