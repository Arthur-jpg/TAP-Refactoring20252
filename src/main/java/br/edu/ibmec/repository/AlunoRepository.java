package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Aluno;

/**
 * Repository Spring Data JPA para a entidade Aluno
 */
@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    
    /**
     * Verifica se existe um aluno com a matrícula informada
     * @param numeroMatricula número da matrícula do aluno
     * @return true se existe, false caso contrário
     */
    boolean existsByNumeroMatricula(int numeroMatricula);
    
    /**
     * Busca aluno por matrícula
     * @param numeroMatricula número da matrícula do aluno
     * @return o aluno encontrado ou null
     */
    Aluno findByNumeroMatricula(int numeroMatricula);
}