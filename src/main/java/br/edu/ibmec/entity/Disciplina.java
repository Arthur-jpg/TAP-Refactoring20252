package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disciplinas")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"curso", "turmas"})
@Builder
public class Disciplina {

    private static final int CODIGO_MINIMO = 1;

    @Id
    @Column(name = "codigo")
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private int codigo;
    
    @Column(name = "nome", nullable = false, length = 100)
    @Setter(AccessLevel.NONE)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    @Setter
    private Curso curso;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private List<Turma> turmas = new ArrayList<>();

    // Construtor personalizado adicional para compatibilidade
    public Disciplina(int codigo, String nome, Curso curso) {
        setCodigo(codigo);
        setNome(nome);
        this.curso = curso;
        this.turmas = new ArrayList<>();
    }

    // Métodos de negócio para turmas
    public void adicionarTurma(Turma turma) {
        validarObjetoNaoNulo(turma, "Turma");
        if (!turmas.contains(turma)) {
            turmas.add(turma);
        }
    }

    public void removerTurma(Turma turma) {
        if (isObjetoValido(turma)) {
            turmas.remove(turma);
        }
    }

    // Setters customizados com validação
    public void setCodigo(int codigo) {
        if (codigo < CODIGO_MINIMO) {
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

    // Métodos de acesso às listas
    public List<Turma> getTurmas() {
        return new ArrayList<>(turmas);
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = criarListaSegura(turmas);
    }

    // Métodos utilitários privados - Clean Code: DRY
    private void validarObjetoNaoNulo(Object objeto, String nomeObjeto) {
        if (objeto == null) {
            throw new IllegalArgumentException(nomeObjeto + " não pode ser nulo");
        }
    }

    private boolean isObjetoValido(Object objeto) {
        return objeto != null;
    }

    private <T> List<T> criarListaSegura(List<T> lista) {
        return lista != null ? new ArrayList<>(lista) : new ArrayList<>();
    }

    // Métodos de consulta
    public int getQuantidadeTurmas() {
        return turmas.size();
    }

    public boolean possuiTurma(Turma turma) {
        return isObjetoValido(turma) && turmas.contains(turma);
    }

}