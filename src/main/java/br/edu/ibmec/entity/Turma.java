package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma turma de disciplina em um determinado período acadêmico.
 * Identificada por código, ano e semestre, e associada a uma disciplina.
 */
@Entity
@Table(name = "turmas")
@IdClass(TurmaId.class)
@Getter
@NoArgsConstructor
public class Turma {
    @Id
    @Column(name = "codigo")
    private int codigo;
    
    @Id
    @Column(name = "ano")
    private int ano;
    
    @Id
    @Column(name = "semestre")
    private int semestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_codigo")
    @Setter
    private Disciplina disciplina;
    
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoes = new ArrayList<>();


    public Turma(int codigo, int ano, int semestre, Disciplina disciplina) {
        this.codigo = codigo;
        this.ano = ano;
        this.semestre = semestre;
        this.disciplina = disciplina;
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

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigo = codigo;
    }

    public void setAno(int ano) {
        if (ano < 1900 || ano > 2100) {
            throw new IllegalArgumentException("Ano deve estar entre 1900 e 2100");
        }
        this.ano = ano;
    }

    public void setSemestre(int semestre) {
        if (semestre < 1 || semestre > 2) {
            throw new IllegalArgumentException("Semestre deve ser 1 ou 2");
        }
        this.semestre = semestre;
    }

    public int getQuantidadeInscricoes() {
        return inscricoes.size();
    }

    public boolean possuiInscricao(Inscricao inscricao) {
        return inscricao != null && inscricoes.contains(inscricao);
    }
}