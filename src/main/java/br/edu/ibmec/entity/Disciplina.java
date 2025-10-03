package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

public class Disciplina {
    private int codigo;
    private String nome;
    private Curso curso;

    private List<Turma> turmas = new ArrayList<Turma>();
    //private List<AlunoMonitor> monitores = new ArrayList<AlunoMonitor>();

    public Disciplina() {

    }

    public Disciplina(int codigo, String nome, Curso curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.curso = curso;
    }

    public void addTurma(Turma turma) {
        turmas.add(turma);
    }

    public void removeTurma(Turma turma) {
        turmas.remove(turma);
    }

    public List<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}