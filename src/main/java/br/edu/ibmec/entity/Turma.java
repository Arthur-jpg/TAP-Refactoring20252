package br.edu.ibmec.entity;

import java.util.ArrayList;
import java.util.List;

public class Turma {
    private int codigo;
    private int ano;
    private int semestre;

    private Disciplina disciplina;
    private List<Inscricao> inscricoes = new ArrayList<Inscricao>();

    public Turma() {

    }

    public Turma(int codigo, int ano, int semestre, Disciplina disciplina) {
        this.codigo = codigo;
        this.ano = ano;
        this.semestre = semestre;
        this.disciplina = disciplina;
    }

    public void addInscricao(Inscricao inscricao) {
        inscricoes.add(inscricao);
    }

    public void removeInscricao(Inscricao inscricao) {
        inscricoes.remove(inscricao);
    }

    public List<Inscricao> getInscricoes() {
        return inscricoes;
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

}