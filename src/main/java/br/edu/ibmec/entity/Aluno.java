package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"inscricoesEmDisciplinas", "cursoMatriculado"})
public class Aluno {

    private static final int MATRICULA_MINIMA = 1;

    @Id
    @Column(name = "matricula")
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private int numeroMatricula;
    
    @Column(name = "nome", nullable = false, length = 100)
    @Setter(AccessLevel.NONE)
    private String nomeCompleto;
    
    @Embedded
    @Setter
    private Data dataNascimento;
    
    @Column(name = "idade")
    @Setter(AccessLevel.NONE)
    private int idadeAtual;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    @Setter
    private EstadoCivil estadoCivilAtual;
    
    @Column(name = "matricula_ativa")
    @Setter
    private boolean possuiMatriculaAtiva;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "aluno_telefones", joinColumns = @JoinColumn(name = "matricula"))
    @Column(name = "telefone")
    @Setter
    private List<String> numerosTelefone = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    @Setter
    private Curso cursoMatriculado;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Inscricao> inscricoesEmDisciplinas = new ArrayList<>();

    // Métodos de negócio para inscrições
    public void adicionarInscricaoEmDisciplina(Inscricao inscricao) {
        validarObjetoNaoNulo(inscricao, "Inscrição");
        if (!inscricoesEmDisciplinas.contains(inscricao)) {
            inscricoesEmDisciplinas.add(inscricao);
        }
    }

    public void removerInscricaoEmDisciplina(Inscricao inscricao) {
        if (isObjetoValido(inscricao)) {
            inscricoesEmDisciplinas.remove(inscricao);
        }
    }

    public List<Inscricao> obterTodasInscricoes() {
        return new ArrayList<>(inscricoesEmDisciplinas);
    }

    public void definirInscricoes(List<Inscricao> novasInscricoes) {
        this.inscricoesEmDisciplinas = criarListaSegura(novasInscricoes);
    }

    // Setters customizados com validação
    public void setNumeroMatricula(int novoNumeroMatricula) {
        if (novoNumeroMatricula < MATRICULA_MINIMA) {
            throw new IllegalArgumentException("Matrícula deve ser um número positivo");
        }
        this.numeroMatricula = novoNumeroMatricula;
    }

    public void setNomeCompleto(String novoNomeCompleto) {
        if (novoNomeCompleto == null || novoNomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
        this.nomeCompleto = novoNomeCompleto.trim();
    }

    public void setIdadeAtual(int novaIdade) {
        if (novaIdade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
        this.idadeAtual = novaIdade;
    }

    // Métodos utilitários privados - Clean Code: métodos pequenos e reutilizáveis
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
    public boolean possuiInscricaoAtiva() {
        return inscricoesEmDisciplinas.stream()
                .anyMatch(this::isObjetoValido);
    }

    public int obterQuantidadeInscricoes() {
        return inscricoesEmDisciplinas.size();
    }

    public boolean isMatriculadoEmCurso() {
        return cursoMatriculado != null;
    }

    public boolean possuiTelefoneContato() {
        return numerosTelefone != null && !numerosTelefone.isEmpty();
    }
}
