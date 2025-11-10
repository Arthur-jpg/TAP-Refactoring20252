package br.edu.ibmec.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "turmas")
@IdClass(TurmaId.class)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"disciplina", "inscricoes"})
public class Turma {

    private static final int CODIGO_MINIMO = 1;
    private static final int ANO_MINIMO = 1900;
    private static final int ANO_MAXIMO = 2100;
    private static final int SEMESTRE_MINIMO = 1;
    private static final int SEMESTRE_MAXIMO = 2;

    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    private int codigo;

    @Id
    @Column(name = "ano")
    @EqualsAndHashCode.Include
    private int ano;

    @Id
    @Column(name = "semestre")
    @EqualsAndHashCode.Include
    private int semestre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciplina_codigo", nullable = false)
    private Disciplina disciplina;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public void setCodigo(int codigo) {
        if (codigo < CODIGO_MINIMO) {
            throw new IllegalArgumentException("Código deve ser positivo");
        }
        this.codigo = codigo;
    }

    public void setAno(int ano) {
        if (ano < ANO_MINIMO || ano > ANO_MAXIMO) {
            throw new IllegalArgumentException("Ano inválido");
        }
        this.ano = ano;
    }

    public void setSemestre(int semestre) {
        if (semestre < SEMESTRE_MINIMO || semestre > SEMESTRE_MAXIMO) {
            throw new IllegalArgumentException("Semestre deve ser 1 ou 2");
        }
        this.semestre = semestre;
    }
}
