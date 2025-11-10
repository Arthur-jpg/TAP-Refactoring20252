package br.edu.ibmec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    boolean existsByCodigo(int codigo);

    Curso findByCodigo(int codigo);
}
