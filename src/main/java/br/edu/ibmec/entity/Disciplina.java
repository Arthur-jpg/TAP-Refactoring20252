package br.edu.ibmec.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma disciplina acadêmica com suas turmas associadas.
 * Cada disciplina pertence a um curso e pode ter múltiplas turmas.
 */
@Entity
@Table(name = "disciplinas")
public class Disciplina {
    @Id
    @Column(name = "codigo")
    private int codigo;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso curso;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turma> turmas = new ArrayList<>();

    public Disciplina() {
    }

    public Disciplina(int codigo, String nome, Curso curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.curso = curso;
    }

    public void adicionarTurma(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula");
        }
        if (!turmas.contains(turma)) {
            turmas.add(turma);
        }
    }

    public void removerTurma(Turma turma) {
        if (turma != null) {
            turmas.remove(turma);
        }
    }

    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas != null ? new ArrayList<>(turmas) : new ArrayList<>();
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

    public int getQuantidadeTurmas() {
        return turmas.size();
    }

    public boolean possuiTurma(Turma turma) {
        return turma != null && turmas.contains(turma);
    }
}