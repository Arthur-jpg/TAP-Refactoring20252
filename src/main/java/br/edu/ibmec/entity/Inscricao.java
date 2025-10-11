package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Representa a inscrição de um aluno em uma turma específica. */
@Entity
@Table(name = "inscricoes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"aluno", "turma"})
public class Inscricao {
    private static final int LIMITE_FALTAS = 15;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "avaliacao1", nullable = false)
    private float avaliacao1;
    
    @Column(name = "avaliacao2", nullable = false)
    private float avaliacao2;
    
    @Column(name = "media")
    private float media;
    
    @Column(name = "num_faltas", nullable = false)
    private int numFaltas;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    private Situacao situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_matricula")
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "turma_codigo"),
        @JoinColumn(name = "turma_ano"),
        @JoinColumn(name = "turma_semestre")
    })
    private Turma turma;

    public Inscricao(float avaliacao1, float avaliacao2, int numFaltas,
                     Situacao situacao, Aluno aluno, Turma turma) {
        this.avaliacao1 = avaliacao1;
        this.avaliacao2 = avaliacao2;
        this.calcularMedia();
        this.numFaltas = numFaltas;
        this.situacao = situacao;
        this.aluno = aluno;
        this.turma = turma;
    }

    public float getAvaliacao1() {
        return avaliacao1;
    }

    public void setAvaliacao1(float avaliacao1) {
        if (avaliacao1 < 0 || avaliacao1 > 10) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0 e 10");
        }
        this.avaliacao1 = avaliacao1;
        calcularMedia();
    }

    public float getAvaliacao2() {
        return avaliacao2;
    }

    public void setAvaliacao2(float avaliacao2) {
        if (avaliacao2 < 0 || avaliacao2 > 10) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0 e 10");
        }
        this.avaliacao2 = avaliacao2;
        calcularMedia();
    }

    private void calcularMedia() {
        this.media = (avaliacao1 + avaliacao2) / 2;
    }

    public int getNumFaltas() {
        return numFaltas;
    }

    public void setNumFaltas(int numFaltas) {
        if (numFaltas < 0) {
            throw new IllegalArgumentException("Número de faltas não pode ser negativo");
        }
        this.numFaltas = numFaltas;
    }


    public boolean isAprovado() {
        return situacao == Situacao.aprovado;
    }

    public boolean temMediaSuficiente() {
        return media >= 7.0f;
    }

    public boolean temFaltasExcessivas() {
        return numFaltas > LIMITE_FALTAS;
    }
}