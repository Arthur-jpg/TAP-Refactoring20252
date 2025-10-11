package br.edu.ibmec.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"inscricoesEmDisciplinas", "cursoMatriculado"})
public class Aluno {

    @Id
    @Column(name = "matricula")
    @EqualsAndHashCode.Include
    private int numeroMatricula;
    
    @Column(name = "nome", nullable = false, length = 100)
    private String nomeCompleto;
    
    @Embedded
    private Data dataNascimento;
    
    @Column(name = "idade")
    private int idadeAtual;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivilAtual;
    
    @Column(name = "matricula_ativa")
    private boolean possuiMatriculaAtiva;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "aluno_telefones", joinColumns = @JoinColumn(name = "matricula"))
    @Column(name = "telefone")
    private List<String> numerosTelefone = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso cursoMatriculado;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoesEmDisciplinas = new ArrayList<>();

    public void adicionarInscricaoEmDisciplina(Inscricao inscricao) {
        validarInscricaoNaoNula(inscricao);
        adicionarInscricaoSeNaoExiste(inscricao);
    }

    public void removerInscricaoEmDisciplina(Inscricao inscricao) {
        if (inscricaoEhValida(inscricao)) {
            inscricoesEmDisciplinas.remove(inscricao);
        }
    }

    public List<Inscricao> obterTodasInscricoes() {
        return new ArrayList<>(inscricoesEmDisciplinas);
    }

    public void definirInscricoes(List<Inscricao> novasInscricoes) {
        this.inscricoesEmDisciplinas = criarListaSeguraDeInscricoes(novasInscricoes);
    }
    
    private void validarInscricaoNaoNula(Inscricao inscricao) {
        if (inscricao == null) {
            throw new IllegalArgumentException("Inscrição não pode ser nula");
        }
    }
    
    private void adicionarInscricaoSeNaoExiste(Inscricao inscricao) {
        if (!inscricoesEmDisciplinas.contains(inscricao)) {
            inscricoesEmDisciplinas.add(inscricao);
        }
    }
    
    private boolean inscricaoEhValida(Inscricao inscricao) {
        return inscricao != null;
    }
    
    private List<Inscricao> criarListaSeguraDeInscricoes(List<Inscricao> inscricoes) {
        return inscricoes != null ? new ArrayList<>(inscricoes) : new ArrayList<>();
    }

    public void definirNumeroMatricula(int novoNumeroMatricula) {
        validarNumeroMatriculaPositivo(novoNumeroMatricula);
        this.numeroMatricula = novoNumeroMatricula;
    }

    public void definirNomeCompleto(String novoNomeCompleto) {
        validarNomeNaoVazio(novoNomeCompleto);
        this.nomeCompleto = novoNomeCompleto.trim();
    }
    
    private void validarNumeroMatriculaPositivo(int numeroMatricula) {
        if (numeroMatricula <= 0){
            throw new IllegalArgumentException("Matrícula deve ser um número positivo");
        }
    }
    
    private void validarNomeNaoVazio(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
    }

    public void definirIdadeAtual(int novaIdade) {
        validarIdadeNaoNegativa(novaIdade);
        this.idadeAtual = novaIdade;
    }
    
    private void validarIdadeNaoNegativa(int idade) {
        if (idade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
    }

    public boolean possuiInscricaoAtiva() {
        return inscricoesEmDisciplinas.stream()
                .anyMatch(this::inscricaoEhValida);
    }

    public int obterQuantidadeInscricoes() {
        return inscricoesEmDisciplinas.size();
    }

    public boolean estaMatriculadoEmCurso() {
        return cursoMatriculado != null;
    }

    public boolean possuiTelefoneContato() {
        return numerosTelefone != null && !numerosTelefone.isEmpty();
    }
    
    private List<String> criarListaSeguraDeTelefones(List<String> telefones) {
        return telefones != null ? new ArrayList<>(telefones) : new ArrayList<>();
    }

}
