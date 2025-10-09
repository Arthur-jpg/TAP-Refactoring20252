package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Disciplina;

/**
 * Repository Spring Data JPA para a entidade Disciplina
 */
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    
    /**
     * Verifica se existe uma disciplina com o código informado
     * @param codigo código da disciplina
     * @return true se existe, false caso contrário
     */
    boolean existsByCodigo(int codigo);
    
    /**
     * Busca disciplina por código
     * @param codigo código da disciplina
     * @return a disciplina encontrada ou null
     */
    Disciplina findByCodigo(int codigo);
}