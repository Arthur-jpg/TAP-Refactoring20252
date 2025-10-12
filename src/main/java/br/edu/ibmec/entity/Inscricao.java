package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;

/** Representa a inscrição de um aluno em uma turma específica. */
@Entity
@Table(name = "inscricoes")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"aluno", "turma"})
public class Inscricao {

    private static final int LIMITE_FALTAS = 15;
    private static final float NOTA_MINIMA = 0.0f;
    private static final float NOTA_MAXIMA = 10.0f;
    private static final float MEDIA_APROVACAO = 7.0f;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;
    
    @Column(name = "avaliacao1", nullable = false)
    @Setter(AccessLevel.NONE)
    private float avaliacao1;
    
    @Column(name = "avaliacao2", nullable = false)
    @Setter(AccessLevel.NONE)
    private float avaliacao2;
    
    @Column(name = "media")
    @Setter(AccessLevel.NONE)
    private float media;
    
    @Column(name = "num_faltas", nullable = false)
    @Setter(AccessLevel.NONE)
    private int numFaltas;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao")
    @Setter
    private Situacao situacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_matricula")
    @Setter
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "turma_ano", referencedColumnName = "ano"),
        @JoinColumn(name = "turma_codigo", referencedColumnName = "codigo"),
        @JoinColumn(name = "turma_semestre", referencedColumnName = "semestre")
    })
    @Setter
    private Turma turma;

    public Inscricao(float avaliacao1, float avaliacao2, int numFaltas,
                     Situacao situacao, Aluno aluno, Turma turma) {
        setAvaliacao1(avaliacao1);
        setAvaliacao2(avaliacao2);
        setNumFaltas(numFaltas);
        this.situacao = situacao;
        this.aluno = aluno;
        this.turma = turma;
    }

    // Setters customizados com validação
    public void setAvaliacao1(float avaliacao1) {
        validarNota(avaliacao1);
        this.avaliacao1 = avaliacao1;
        calcularMedia();
    }

    public void setAvaliacao2(float avaliacao2) {
        validarNota(avaliacao2);
        this.avaliacao2 = avaliacao2;
        calcularMedia();
    }

    public void setNumFaltas(int numFaltas) {
        if (numFaltas < 0) {
            throw new IllegalArgumentException("Número de faltas não pode ser negativo");
        }
        this.numFaltas = numFaltas;
    }

    // Métodos privados
    private void validarNota(float nota) {
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new IllegalArgumentException("Avaliação deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA);
        }
    }

    private void calcularMedia() {
        this.media = (avaliacao1 + avaliacao2) / 2;
    }

    // Métodos de consulta
    public boolean isAprovado() {
        return situacao == Situacao.aprovado;
    }

    public boolean temMediaSuficiente() {
        return media >= MEDIA_APROVACAO;
    }

    public boolean temFaltasExcessivas() {
        return numFaltas > LIMITE_FALTAS;
    }
}