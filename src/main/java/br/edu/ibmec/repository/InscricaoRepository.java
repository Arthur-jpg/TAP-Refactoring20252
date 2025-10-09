package br.edu.ibmec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Inscricao;

/**
 * Repository Spring Data JPA para a entidade Inscricao
 */
@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    
    /**
     * Busca inscrições por matrícula do aluno
     * @param matricula matrícula do aluno
     * @return lista de inscrições do aluno
     */
    List<Inscricao> findByAlunoMatricula(int matricula);
    
    /**
     * Busca inscrições por código da turma
     * @param codigoTurma código da turma
     * @return lista de inscrições da turma
     */
    List<Inscricao> findByTurmaCodigo(int codigoTurma);
}