package br.edu.ibmec.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Representa um aluno da universidade com suas informações pessoais,
 * curso associado e inscrições em disciplinas.
 */
@Entity
@Table(name = "alunos")
public class Aluno {
    @Id
    @Column(name = "matricula")
    private int matricula;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @Embedded
    private Data dataNascimento;
    
    @Column(name = "idade")
    private int idade;
    
    @Column(name = "matricula_ativa")
    private boolean isMatriculaAtiva;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;
    
    @ElementCollection
    @CollectionTable(name = "aluno_telefones", joinColumns = @JoinColumn(name = "matricula"))
    @Column(name = "telefone")
    private Vector<String> telefones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso curso;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public Aluno() {
    }

    public Aluno(int matricula, String nome, Data dataNascimento,
                 boolean matriculaAtiva, EstadoCivil estadoCivil, Curso curso,
                 Vector<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.isMatriculaAtiva = matriculaAtiva;
        this.estadoCivil = estadoCivil;
        this.curso = curso;
        this.idade = 0;
        this.telefones = telefones;
    }

    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição não pode ser nula");
        }
        if (!inscricoes.contains(inscricao)) {
            inscricoes.add(inscricao);
        }
    }

    public void removerInscricao(Inscricao inscricao) {
        if (inscricao != null) {
            inscricoes.remove(inscricao);
        }
    }

    public List<Inscricao> getInscricoes() {
        return new ArrayList<>(inscricoes);
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = inscricoes != null ? new ArrayList<>(inscricoes) : new ArrayList<>();
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        if (matricula <= 0) {
            throw new IllegalArgumentException("Matrícula deve ser um número positivo");
        }
        this.matricula = matricula;
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
        if (idade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
        this.idade = idade;
    }

    public boolean isMatriculaAtiva() {
        return isMatriculaAtiva;
    }

    public void setMatriculaAtiva(boolean matriculaAtiva) {
        this.isMatriculaAtiva = matriculaAtiva;
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
        return telefones != null ? new Vector<>(telefones) : new Vector<>();
    }

    public void setTelefones(Vector<String> telefones) {
        this.telefones = telefones != null ? new Vector<>(telefones) : new Vector<>();
    }

    public boolean temInscricaoAtiva() {
        return inscricoes.stream()
                .anyMatch(inscricao -> inscricao != null);
    }

    public int getQuantidadeInscricoes() {
        return inscricoes.size();
    }
}