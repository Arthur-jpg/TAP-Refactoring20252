package br.edu.ibmec.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um curso universitário com suas disciplinas e alunos matriculados.
 * Gerencia a relação entre alunos e disciplinas dentro do contexto do curso.
 * 
 * Esta classe segue os princípios de Clean Code:
 * - Nomes significativos e intenção clara
 * - Responsabilidade única (representar um curso)
 * - Encapsulamento adequado
 * - Métodos pequenos e focados
 */
@Entity
@Table(name = "cursos")
public class Curso {
    
    // Identificador único do curso
    @Id
    @Column(name = "codigo")
    private int codigoCurso;
    
    // Informações básicas do curso
    @Column(name = "nome", nullable = false, length = 100)
    private String nomeCurso;
    
    // Relacionamentos - alunos matriculados no curso
    @OneToMany(mappedBy = "cursoMatriculado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Aluno> alunosMatriculados = new ArrayList<>();
    
    // Relacionamentos - disciplinas oferecidas pelo curso
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Disciplina> disciplinasOfertadas = new ArrayList<>();

    // Construtores
    public Curso() {
    }

    public Curso(int codigoCurso, String nomeCurso) {
        this.codigoCurso = codigoCurso;
        this.nomeCurso = nomeCurso;
    }

    // Métodos de gerenciamento de alunos
    public void adicionarAlunoMatriculado(Aluno aluno) {
        validarAlunoNaoNulo(aluno);
        adicionarAlunoSeNaoExiste(aluno);
    }

    public void removerAlunoMatriculado(Aluno aluno) {
        if (alunoEhValido(aluno)) {
            alunosMatriculados.remove(aluno);
        }
    }

    // Métodos de gerenciamento de disciplinas
    public void adicionarDisciplinaOfertada(Disciplina disciplina) {
        validarDisciplinaNaoNula(disciplina);
        adicionarDisciplinaSeNaoExiste(disciplina);
    }

    public void removerDisciplinaOfertada(Disciplina disciplina) {
        if (disciplinaEhValida(disciplina)) {
            disciplinasOfertadas.remove(disciplina);
        }
    }
    
    // Métodos auxiliares privados para validação de alunos
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
    
    // Métodos auxiliares privados para validação de disciplinas
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

    // Getters e setters para listas de relacionamentos
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

    // Getters e setters para propriedades básicas
    public int obterCodigoCurso() {
        return codigoCurso;
    }

    public void definirCodigoCurso(int novoCodigoCurso) {
        validarCodigoPositivo(novoCodigoCurso);
        this.codigoCurso = novoCodigoCurso;
    }

    public String obterNomeCurso() {
        return nomeCurso;
    }

    public void definirNomeCurso(String novoNomeCurso) {
        validarNomeNaoVazio(novoNomeCurso);
        this.nomeCurso = novoNomeCurso.trim();
    }
    
    // Métodos auxiliares para criação de listas seguras
    private List<Aluno> criarListaSeguraDeAlunos(List<Aluno> alunos) {
        return alunos != null ? new ArrayList<>(alunos) : new ArrayList<>();
    }
    
    private List<Disciplina> criarListaSeguraDeDisciplinas(List<Disciplina> disciplinas) {
        return disciplinas != null ? new ArrayList<>(disciplinas) : new ArrayList<>();
    }
    
    // Métodos de validação privados
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

    // Métodos de consulta e regras de negócio
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

    // Sobrescrita de métodos Object para comparação e debug
    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) return true;
        if (outroObjeto == null || getClass() != outroObjeto.getClass()) return false;
        
        Curso outroCurso = (Curso) outroObjeto;
        return codigoCurso == outroCurso.codigoCurso;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codigoCurso);
    }

    @Override
    public String toString() {
        return String.format("Curso{codigo=%d, nome='%s', alunos=%d, disciplinas=%d}", 
                codigoCurso, 
                nomeCurso, 
                obterQuantidadeAlunosMatriculados(),
                obterQuantidadeDisciplinasOfertadas());
    }
}