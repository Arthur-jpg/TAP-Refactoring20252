package br.edu.ibmec.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"curso", "turmas"})
public class Disciplina {

    private static final int CODIGO_MINIMO = 1;

    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    private int codigo;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "valor_base")
    private BigDecimal valorBase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_codigo", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turma> turmas = new ArrayList<>();

    public void setCodigo(int codigo) {
        if (codigo < CODIGO_MINIMO) {
            throw new IllegalArgumentException("Código deve ser positivo");
        }
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }

    public void setValorBase(BigDecimal valorBase) {
        if (valorBase == null || valorBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor base da disciplina deve ser zero ou positivo");
        }
        this.valorBase = valorBase;
    }
}
