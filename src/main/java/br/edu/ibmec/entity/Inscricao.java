package br.edu.ibmec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "inscricoes",
        uniqueConstraints = @UniqueConstraint(name = "uk_inscricao_aluno_turma",
                columnNames = {"aluno_matricula", "turma_codigo", "turma_ano", "turma_semestre"}))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"aluno", "turma"})
public class Inscricao {

    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aluno_matricula", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
        @JoinColumn(name = "turma_codigo", referencedColumnName = "codigo", nullable = false),
        @JoinColumn(name = "turma_ano", referencedColumnName = "ano", nullable = false),
        @JoinColumn(name = "turma_semestre", referencedColumnName = "semestre", nullable = false)
    })
    private Turma turma;

    @Column(name = "nota_av1")
    private Double notaAv1;

    @Column(name = "nota_av2")
    private Double notaAv2;

    @Column(name = "nota_recuperacao")
    private Double notaRecuperacao;

    @Column(name = "media_final")
    private Double mediaFinal;

    @Column(name = "status_aprovacao", length = 20)
    private String statusAprovacao;

    public void setId(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Id da inscrição deve ser positivo");
        }
        this.id = id;
    }
}
