package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"curso", "turmas"})
public class Disciplina {
    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    private int codigo;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso curso;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turma> turmas = new ArrayList<>();

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

    // Getters/Setters com cópia defensiva para lista
    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas != null ? new ArrayList<>(turmas) : new ArrayList<>();
    }

    // Setters customizados com validação
    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigo = codigo;
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

    // Construtor personalizado para compatibilidade
    public Disciplina(int codigo, String nome, Curso curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.curso = curso;
    }


}