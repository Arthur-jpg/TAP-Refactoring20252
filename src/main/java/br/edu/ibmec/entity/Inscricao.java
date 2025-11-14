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

    public void setId(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Id da inscrição deve ser positivo");
        }
        this.id = id;
    }
}
