package br.edu.ibmec.entity;


public class Inscricao {
    private float avaliacao1;
    private float avaliacao2;
    private float media;
    private int numFaltas;
    private String situacao;

    private Aluno aluno;
    private Turma turma;

    // private Avaliacao avaliacao;

    public Inscricao() {

    }

    public Inscricao(float avaliacao1, float avaliacao2, int numFaltas,
                     String situacao, Aluno aluno, Turma turma) {
        this.avaliacao1 = avaliacao1;
        this.avaliacao2 = avaliacao2;
        this.media = (avaliacao1 + avaliacao2) / 2;
        this.numFaltas = numFaltas;
        this.situacao = situacao;
        this.aluno = aluno;
        this.turma = turma;
    }

    public float getAvaliacao1() {
        return avaliacao1;
    }

    public void setAvaliacao1(float avaliacao1) {
        this.avaliacao1 = avaliacao1;
    }

    public float getAvaliacao2() {
        return avaliacao2;
    }

    public void setAvaliacao2(float avaliacao2) {
        this.avaliacao2 = avaliacao2;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public int getNumFaltas() {
        return numFaltas;
    }

    public void setNumFaltas(int numFaltas) {
        this.numFaltas = numFaltas;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

}