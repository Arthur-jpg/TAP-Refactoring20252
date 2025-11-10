package br.edu.ibmec.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ibmec.entity.Inscricao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByAlunoMatricula(int matricula);

    List<Inscricao> findByTurmaCodigoAndTurmaAnoAndTurmaSemestre(int codigo, int ano, int semestre);

    boolean existsByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(int matricula, int codigo, int ano, int semestre);

    Optional<Inscricao> findByAlunoMatriculaAndTurmaCodigoAndTurmaAnoAndTurmaSemestre(int matricula, int codigo, int ano, int semestre);
}
