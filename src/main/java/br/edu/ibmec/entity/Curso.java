package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um curso universitário com suas disciplinas e alunos matriculados.
 * Gerencia a relação entre alunos e disciplinas dentro do contexto do curso.
 */
public class Curso {
    private int codigo;
    private String nome;
    private List<Aluno> alunos = new ArrayList<>();
    private List<Disciplina> disciplinas = new ArrayList<>();

    public Curso() {
    }

    public Curso(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public void adicionarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (!alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void removerAluno(Aluno aluno) {
        if (aluno != null) {
            alunos.remove(aluno);
        }
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        if (disciplina == null) {
            throw new IllegalArgumentException("Disciplina não pode ser nula");
        }
        if (!disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(Disciplina disciplina) {
        if (disciplina != null) {
            disciplinas.remove(disciplina);
        }
    }

    public List<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos != null ? new ArrayList<>(alunos) : new ArrayList<>();
    }

    public List<Disciplina> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas != null ? new ArrayList<>(disciplinas) : new ArrayList<>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nome = nome.trim();
    }

    public int getQuantidadeAlunos() {
        return alunos.size();
    }

    public int getQuantidadeDisciplinas() {
        return disciplinas.size();
    }

    public boolean possuiAluno(Aluno aluno) {
        return aluno != null && alunos.contains(aluno);
    }

    public boolean possuiDisciplina(Disciplina disciplina) {
        return disciplina != null && disciplinas.contains(disciplina);
    }
}