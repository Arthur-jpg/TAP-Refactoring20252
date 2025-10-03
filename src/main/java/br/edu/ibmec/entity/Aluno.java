package br.edu.ibmec.entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Aluno {
    private int matricula;
    private String nome;
    private Data dataNascimento;
    private int idade;
    private boolean matriculaAtiva;
    private EstadoCivil estadoCivil;
    private Vector<String> telefones;


    private Curso curso;

    private List<Inscricao> inscricoes = new ArrayList<Inscricao>();

    public Aluno() {

    }

    public Aluno(int matricula, String nome, Data dataNascimento,
                 boolean matriculaAtiva, EstadoCivil estadoCivil, Curso curso,
                 Vector<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.matriculaAtiva = matriculaAtiva;
        this.estadoCivil = estadoCivil;
        this.curso = curso;

        this.idade = 0;
        this.telefones = telefones;
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

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Data getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Data dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isMatriculaAtiva() {
        return matriculaAtiva;
    }

    public void setMatriculaAtiva(boolean matriculaAtiva) {
        this.matriculaAtiva = matriculaAtiva;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Vector<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Vector<String> telefones) {
        this.telefones = telefones;
    }

}