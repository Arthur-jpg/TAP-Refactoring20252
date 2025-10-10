package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Curso;

/**
 * Repository Spring Data JPA para a entidade Curso
 * Substitui o DAO tradicional por uma abordagem mais moderna
 */
@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    
    /**
     * Verifica se existe um curso com o código informado
     * @param codigoCurso código do curso
     * @return true se existe, false caso contrário
     */
    boolean existsByCodigoCurso(int codigoCurso);
    
    /**
     * Busca curso por código
     * @param codigoCurso código do curso
     * @return o curso encontrado ou null
     */
    Curso findByCodigoCurso(int codigoCurso);
}