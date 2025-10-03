package br.edu.ibmec.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="inscricao")
public class InscricaoDTO {
    private float avaliacao1;
    private float avaliacao2;
    private float media;
    private int numFaltas;
    private String situacao;

    private int aluno;
    private int codigo;
    private int ano;
    private int semestre;

    // private Avaliacao avaliacao;

    public InscricaoDTO() {

    }

    public InscricaoDTO(float avaliacao1, float avaliacao2, int numFaltas, String situacao, int aluno, int codigoTurma,
                        int ano, int semestre) {
        this.avaliacao1 = avaliacao1;
        this.avaliacao2 = avaliacao2;
        //this.media = media;
        this.numFaltas = numFaltas;
        this.situacao = situacao;
        this.aluno = aluno;
        this.codigo = codigoTurma;
        this.ano = ano;
        this.semestre = semestre;
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

    public int getAluno() {
        return aluno;
    }

    public void setAluno(int aluno) {
        this.aluno = aluno;
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

}