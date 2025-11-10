package br.edu.ibmec.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "inscricoes")
public class Aluno {

    private static final int MATRICULA_MINIMA = 1;

    @Id
    @Column(name = "matricula")
    @EqualsAndHashCode.Include
    private int matricula;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public void setMatricula(int matricula) {
        if (matricula < MATRICULA_MINIMA) {
            throw new IllegalArgumentException("Matrícula deve ser positiva");
        }
        this.matricula = matricula;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        this.nome = nome.trim();
    }

    public void adicionarInscricao(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição não pode ser nula");
        }
        inscricoes.add(inscricao);
        inscricao.setAluno(this);
    }

    public void removerInscricao(Inscricao inscricao) {
        if (inscricao == null) {
            return;
        }
        inscricoes.remove(inscricao);
        inscricao.setAluno(null);
    }
}
