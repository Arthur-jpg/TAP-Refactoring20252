package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"alunosMatriculados", "disciplinasOfertadas"})
public class Curso {
    
    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    private int codigoCurso;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nomeCurso;
    
    @OneToMany(mappedBy = "cursoMatriculado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aluno> alunosMatriculados = new ArrayList<>();
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Disciplina> disciplinasOfertadas = new ArrayList<>();

    public Curso (int codigoCurso, String nome ) {
        this.codigoCurso = codigoCurso;
        this.nomeCurso = nome;
    }

    public void adicionarAlunoMatriculado(Aluno aluno) {
        validarAlunoNaoNulo(aluno);
        adicionarAlunoSeNaoExiste(aluno);
    }

    public void removerAlunoMatriculado(Aluno aluno) {
        if (alunoEhValido(aluno)) {
            alunosMatriculados.remove(aluno);
        }
    }

    public void adicionarDisciplinaOfertada(Disciplina disciplina) {
        validarDisciplinaNaoNula(disciplina);
        adicionarDisciplinaSeNaoExiste(disciplina);
    }

    public void removerDisciplinaOfertada(Disciplina disciplina) {
        if (disciplinaEhValida(disciplina)) {
            disciplinasOfertadas.remove(disciplina);
        }
    }
    
    private void validarAlunoNaoNulo(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
    }
    
    private void adicionarAlunoSeNaoExiste(Aluno aluno) {
        if (!alunosMatriculados.contains(aluno)) {
            alunosMatriculados.add(aluno);
        }
    }
    
    private boolean alunoEhValido(Aluno aluno) {
        return aluno != null;
    }
    
    private void validarDisciplinaNaoNula(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
    }
    
    private void adicionarDisciplinaSeNaoExiste(Disciplina disciplina) {
        if (!disciplinasOfertadas.contains(disciplina)) {
            disciplinasOfertadas.add(disciplina);
        }
    }
    
    private boolean disciplinaEhValida(Disciplina disciplina) {
        return disciplina != null;
    }

    public List<Aluno> obterTodosAlunosMatriculados() {
        return criarListaSeguraDeAlunos(alunosMatriculados);
    }

    public void definirAlunosMatriculados(List<Aluno> novosAlunos) {
        this.alunosMatriculados = criarListaSeguraDeAlunos(novosAlunos);
    }

    public List<Disciplina> obterTodasDisciplinasOfertadas() {
        return criarListaSeguraDeDisciplinas(disciplinasOfertadas);
    }

    public void definirDisciplinasOfertadas(List<Disciplina> novasDisciplinas) {
        this.disciplinasOfertadas = criarListaSeguraDeDisciplinas(novasDisciplinas);
    }

    public void definirCodigoCurso(int novoCodigoCurso) {
        validarCodigoPositivo(novoCodigoCurso);
        this.codigoCurso = novoCodigoCurso;
    }
    public void definirNomeCurso(String novoNomeCurso) {
        validarNomeNaoVazio(novoNomeCurso);
        this.nomeCurso = novoNomeCurso.trim();
    }
    
    private List<Aluno> criarListaSeguraDeAlunos(List<Aluno> alunos) {
        return alunos != null ? new ArrayList<>(alunos) : new ArrayList<>();
    }
    
    private List<Disciplina> criarListaSeguraDeDisciplinas(List<Disciplina> disciplinas) {
        return disciplinas != null ? new ArrayList<>(disciplinas) : new ArrayList<>();
    }
    
    private void validarCodigoPositivo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
    }
    
    private void validarNomeNaoVazio(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
    }

    public int obterQuantidadeAlunosMatriculados() {
        return alunosMatriculados.size();
    }

    public int obterQuantidadeDisciplinasOfertadas() {
        return disciplinasOfertadas.size();
    }

    public boolean possuiAlunoMatriculado(Aluno aluno) {
        return alunoEhValido(aluno) && alunosMatriculados.contains(aluno);
    }

    public boolean possuiDisciplinaOfertada(Disciplina disciplina) {
        return disciplinaEhValida(disciplina) && disciplinasOfertadas.contains(disciplina);
    }

    public boolean temAlunosMatriculados() {
        return !alunosMatriculados.isEmpty();
    }

    public boolean temDisciplinasOfertadas() {
        return !disciplinasOfertadas.isEmpty();
    }

    public boolean cursoEstaAtivo() {
        return temAlunosMatriculados() || temDisciplinasOfertadas();
    }

}