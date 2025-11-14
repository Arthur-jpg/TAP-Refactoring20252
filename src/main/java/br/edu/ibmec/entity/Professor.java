package br.edu.ibmec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "turmas")
public class Professor {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private List<Turma> turmas = new ArrayList<>();

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }

    public void setId(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Id do professor deve ser positivo");
        }
        this.id = id;
    }
}
