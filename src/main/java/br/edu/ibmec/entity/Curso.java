package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"alunosMatriculados", "disciplinasOfertadas"})
public class Curso {
    
    private static final int CODIGO_MINIMO = 1;

    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private int codigoCurso;
    
    @Column(name = "nome", nullable = false, length = 100)
    @Setter(AccessLevel.NONE)
    private String nomeCurso;
    
    @OneToMany(mappedBy = "cursoMatriculado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Aluno> alunosMatriculados = new ArrayList<>();
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Disciplina> disciplinasOfertadas = new ArrayList<>();

    // Construtor customizado
    public Curso(int codigoCurso, String nome) {
        setCodigoCurso(codigoCurso);
        setNomeCurso(nome);
    }

    // Métodos de negócio para alunos
    public void adicionarAlunoMatriculado(Aluno aluno) {
        validarObjetoNaoNulo(aluno, "Aluno");
        if (!alunosMatriculados.contains(aluno)) {
            alunosMatriculados.add(aluno);
        }
    }

    public void removerAlunoMatriculado(Aluno aluno) {
        if (isObjetoValido(aluno)) {
            alunosMatriculados.remove(aluno);
        }
    }

    // Métodos de negócio para disciplinas
    public void adicionarDisciplinaOfertada(Disciplina disciplina) {
        validarObjetoNaoNulo(disciplina, "Disciplina");
        if (!disciplinasOfertadas.contains(disciplina)) {
            disciplinasOfertadas.add(disciplina);
        }
    }

    public void removerDisciplinaOfertada(Disciplina disciplina) {
        if (isObjetoValido(disciplina)) {
            disciplinasOfertadas.remove(disciplina);
        }
    }

    // Setters customizados com validação
    public void setCodigoCurso(int novoCodigoCurso) {
        if (novoCodigoCurso < CODIGO_MINIMO) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigoCurso = novoCodigoCurso;
    }

    public void setNomeCurso(String novoNomeCurso) {
        if (novoNomeCurso == null || novoNomeCurso.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nomeCurso = novoNomeCurso.trim();
    }

    // Métodos utilitários privados - Clean Code: DRY
    private void validarObjetoNaoNulo(Object objeto, String nomeObjeto) {
        if (objeto == null) {
            throw new IllegalArgumentException(nomeObjeto + " não pode ser nulo");
        }
    }

    private boolean isObjetoValido(Object objeto) {
        return objeto != null;
    }

    private <T> List<T> criarListaSegura(List<T> lista) {
        return lista != null ? new ArrayList<>(lista) : new ArrayList<>();
    }

    // Métodos de acesso às listas
    public List<Aluno> obterTodosAlunosMatriculados() {
        return new ArrayList<>(alunosMatriculados);
    }

    public void setAlunosMatriculados(List<Aluno> novosAlunos) {
        this.alunosMatriculados = criarListaSegura(novosAlunos);
    }

    public List<Disciplina> obterTodasDisciplinasOfertadas() {
        return new ArrayList<>(disciplinasOfertadas);
    }

    public void setDisciplinasOfertadas(List<Disciplina> novasDisciplinas) {
        this.disciplinasOfertadas = criarListaSegura(novasDisciplinas);
    }

    // Métodos de consulta
    public int obterQuantidadeAlunosMatriculados() {
        return alunosMatriculados.size();
    }

    public int obterQuantidadeDisciplinasOfertadas() {
        return disciplinasOfertadas.size();
    }

    public boolean possuiAlunoMatriculado(Aluno aluno) {
        return isObjetoValido(aluno) && alunosMatriculados.contains(aluno);
    }

    public boolean possuiDisciplinaOfertada(Disciplina disciplina) {
        return isObjetoValido(disciplina) && disciplinasOfertadas.contains(disciplina);
    }

}