package br.edu.ibmec.dao.impl;

import br.edu.ibmec.dao.InscricaoDAO;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Implementação JPA do DAO para a entidade Inscricao.
 * Utiliza EntityManager para operações de persistência.
 */
@Repository
@Transactional
public class InscricaoDAOImpl implements InscricaoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Inscricao inscricao) throws DaoException {
        try {
            // Verificar se já existe inscrição para o mesmo aluno na mesma turma
            if (existsByAlunoAndTurma(
                    inscricao.getAluno().getMatricula(),
                    inscricao.getTurma().getCodigo(),
                    inscricao.getTurma().getAno(),
                    inscricao.getTurma().getSemestre())) {
                throw new DaoException("Aluno já inscrito nesta turma");
            }
            entityManager.persist(inscricao);
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao salvar inscrição: " + e.getMessage());
        }
    }

    @Override
    public Inscricao update(Inscricao inscricao) throws DaoException {
        try {
            Inscricao updated = entityManager.merge(inscricao);
            entityManager.flush();
            return updated;
        } catch (Exception e) {
            throw new DaoException("Erro ao atualizar inscrição: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscricao> findById(Long id) throws DaoException {
        try {
            Inscricao inscricao = entityManager.find(Inscricao.class, id);
            return Optional.ofNullable(inscricao);
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar inscrição: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Inscricao> findByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                                                 int anoTurma, int semestreTurma) throws DaoException {
        try {
            TypedQuery<Inscricao> query = entityManager.createQuery(
                "SELECT i FROM Inscricao i WHERE i.aluno.matricula = :matricula " +
                "AND i.turma.codigo = :codigo AND i.turma.ano = :ano AND i.turma.semestre = :semestre",
                Inscricao.class);
            query.setParameter("matricula", matriculaAluno);
            query.setParameter("codigo", codigoTurma);
            query.setParameter("ano", anoTurma);
            query.setParameter("semestre", semestreTurma);
            
            var result = query.getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar inscrição por aluno e turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Inscricao> findAll() throws DaoException {
        try {
            TypedQuery<Inscricao> query = entityManager.createQuery(
                "SELECT i FROM Inscricao i", Inscricao.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao listar inscrições: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) throws DaoException {
        try {
            Optional<Inscricao> inscricao = findById(id);
            if (inscricao.isEmpty()) {
                throw new DaoException("Inscrição com ID " + id + " não encontrada");
            }
            
            entityManager.remove(inscricao.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover inscrição: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Inscricao> findByAluno(int matriculaAluno) throws DaoException {
        try {
            TypedQuery<Inscricao> query = entityManager.createQuery(
                "SELECT i FROM Inscricao i WHERE i.aluno.matricula = :matricula", Inscricao.class);
            query.setParameter("matricula", matriculaAluno);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar inscrições do aluno: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Inscricao> findByTurma(int codigoTurma, int anoTurma, int semestreTurma) throws DaoException {
        try {
            TypedQuery<Inscricao> query = entityManager.createQuery(
                "SELECT i FROM Inscricao i WHERE i.turma.codigo = :codigo " +
                "AND i.turma.ano = :ano AND i.turma.semestre = :semestre", Inscricao.class);
            query.setParameter("codigo", codigoTurma);
            query.setParameter("ano", anoTurma);
            query.setParameter("semestre", semestreTurma);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Erro ao buscar inscrições da turma: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                                       int anoTurma, int semestreTurma) throws DaoException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(i) FROM Inscricao i WHERE i.aluno.matricula = :matricula " +
                "AND i.turma.codigo = :codigo AND i.turma.ano = :ano AND i.turma.semestre = :semestre",
                Long.class);
            query.setParameter("matricula", matriculaAluno);
            query.setParameter("codigo", codigoTurma);
            query.setParameter("ano", anoTurma);
            query.setParameter("semestre", semestreTurma);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new DaoException("Erro ao verificar existência da inscrição: " + e.getMessage());
        }
    }

    @Override
    public void deleteByAlunoAndTurma(int matriculaAluno, int codigoTurma, 
                                    int anoTurma, int semestreTurma) throws DaoException {
        try {
            Optional<Inscricao> inscricao = findByAlunoAndTurma(matriculaAluno, codigoTurma, anoTurma, semestreTurma);
            if (inscricao.isEmpty()) {
                throw new DaoException("Inscrição não encontrada para o aluno " + matriculaAluno + " na turma " + 
                                     codigoTurma + "/" + anoTurma + "/" + semestreTurma);
            }
            
            entityManager.remove(inscricao.get());
            entityManager.flush();
        } catch (Exception e) {
            throw new DaoException("Erro ao remover inscrição: " + e.getMessage());
        }
    }
}