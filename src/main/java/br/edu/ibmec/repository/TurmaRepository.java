package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Turma;
import br.edu.ibmec.entity.TurmaId;

/**
 * Repository Spring Data JPA para a entidade Turma
 */
@Repository
public interface TurmaRepository extends JpaRepository<Turma, TurmaId> {
    
    /**
     * Busca turma por código, ano e semestre
     * @param codigo código da turma
     * @param ano ano da turma
     * @param semestre semestre da turma
     * @return a turma encontrada ou null
     */
    Turma findByCodigoAndAnoAndSemestre(int codigo, int ano, int semestre);
}