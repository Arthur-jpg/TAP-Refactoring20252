package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
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

    private static final int CODIGO_MINIMO = 1;
    private static final int ANO_MINIMO = 1900;
    private static final int ANO_MAXIMO = 2100;
    private static final int SEMESTRE_MINIMO = 1;
    private static final int SEMESTRE_MAXIMO = 2;

    @Id
    @Column(name = "codigo")
    @Setter(AccessLevel.NONE)
    private int codigo;
    
    @Id
    @Column(name = "ano")
    @Setter(AccessLevel.NONE)
    private int ano;
    
    @Id
    @Column(name = "semestre")
    @Setter(AccessLevel.NONE)
    private int semestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_codigo")
    @Setter
    private Disciplina disciplina;
    
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Inscricao> inscricoes = new ArrayList<>();

    public Turma(int codigo, int ano, int semestre, Disciplina disciplina) {
        setCodigo(codigo);
        setAno(ano);
        setSemestre(semestre);
        this.disciplina = disciplina;
    }

    // Métodos de negócio para inscrições
    public void adicionarInscricao(Inscricao inscricao) {
        validarObjetoNaoNulo(inscricao, "Inscrição");
        if (!inscricoes.contains(inscricao)) {
            inscricoes.add(inscricao);
        }
    }

    public void removerInscricao(Inscricao inscricao) {
        if (isObjetoValido(inscricao)) {
            inscricoes.remove(inscricao);
        }
    }

    // Setters customizados com validação
    public void setCodigo(int codigo) {
        if (codigo < CODIGO_MINIMO) {
            throw new IllegalArgumentException("Código deve ser um número positivo");
        }
        this.codigo = codigo;
    }

    public void setAno(int ano) {
        if (ano < ANO_MINIMO || ano > ANO_MAXIMO) {
            throw new IllegalArgumentException("Ano deve estar entre " + ANO_MINIMO + " e " + ANO_MAXIMO);
        }
        this.ano = ano;
    }

    public void setSemestre(int semestre) {
        if (semestre < SEMESTRE_MINIMO || semestre > SEMESTRE_MAXIMO) {
            throw new IllegalArgumentException("Semestre deve ser " + SEMESTRE_MINIMO + " ou " + SEMESTRE_MAXIMO);
        }
        this.semestre = semestre;
    }

    // Métodos de acesso às listas
    public List<Inscricao> getInscricoes() {
        return new ArrayList<>(inscricoes);
    }

    public void setInscricoes(List<Inscricao> inscricoes) {
        this.inscricoes = criarListaSegura(inscricoes);
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
    public int getQuantidadeInscricoes() {
        return inscricoes.size();
    }

    public boolean possuiInscricao(Inscricao inscricao) {
        return isObjetoValido(inscricao) && inscricoes.contains(inscricao);
    }
}