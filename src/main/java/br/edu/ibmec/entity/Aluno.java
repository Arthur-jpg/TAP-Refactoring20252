package br.edu.ibmec.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alunos")
public class Aluno {

    // Identificadores únicos
    @Id
    @Column(name = "matricula")
    private int numeroMatricula;
    
    // Informações pessoais
    @Column(name = "nome", nullable = false, length = 100)
    private String nomeCompleto;
    
    @Embedded
    private Data dataNascimento;
    
    @Column(name = "idade")
    private int idadeAtual;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivilAtual;
    
    // Status acadêmico
    @Column(name = "matricula_ativa")
    private boolean possuiMatriculaAtiva;
    
    // Informações de contato
    @ElementCollection
    @CollectionTable(name = "aluno_telefones", joinColumns = @JoinColumn(name = "matricula"))
    @Column(name = "telefone")
    private List<String> numerosTelefone = new ArrayList<>();
    
    // Relacionamentos acadêmicos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_codigo")
    private Curso cursoMatriculado;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inscricao> inscricoesEmDisciplinas = new ArrayList<>();

    // Construtores
    public Aluno() {
    }

    public Aluno(int numeroMatricula, String nomeCompleto, Data dataNascimento,
                 boolean possuiMatriculaAtiva, EstadoCivil estadoCivilAtual, 
                 Curso cursoMatriculado, List<String> numerosTelefone) {
        this.numeroMatricula = numeroMatricula;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.possuiMatriculaAtiva = possuiMatriculaAtiva;
        this.estadoCivilAtual = estadoCivilAtual;
        this.cursoMatriculado = cursoMatriculado;
        this.idadeAtual = 0;
        this.numerosTelefone = numerosTelefone != null ? new ArrayList<>(numerosTelefone) : new ArrayList<>();
    }

    // Métodos de gerenciamento de inscrições
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
    
    // Métodos auxiliares privados para validação
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

    // Getters e setters para identificação
    public int obterNumeroMatricula() {
        return numeroMatricula;
    }

    public void definirNumeroMatricula(int novoNumeroMatricula) {
        validarNumeroMatriculaPositivo(novoNumeroMatricula);
        this.numeroMatricula = novoNumeroMatricula;
    }

    public String obterNomeCompleto() {
        return nomeCompleto;
    }

    public void definirNomeCompleto(String novoNomeCompleto) {
        validarNomeNaoVazio(novoNomeCompleto);
        this.nomeCompleto = novoNomeCompleto.trim();
    }
    
    // Métodos de validação privados
    private void validarNumeroMatriculaPositivo(int numeroMatricula) {
        if (numeroMatricula <= 0) {
            throw new IllegalArgumentException("Matrícula deve ser um número positivo");
        }
    }
    
    private void validarNomeNaoVazio(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
    }

    // Getters e setters para informações pessoais
    public Data obterDataNascimento() {
        return dataNascimento;
    }

    public void definirDataNascimento(Data novaDataNascimento) {
        this.dataNascimento = novaDataNascimento;
    }

    public int obterIdadeAtual() {
        return idadeAtual;
    }

    public void definirIdadeAtual(int novaIdade) {
        validarIdadeNaoNegativa(novaIdade);
        this.idadeAtual = novaIdade;
    }

    public boolean possuiMatriculaAtiva() {
        return possuiMatriculaAtiva;
    }

    public void definirStatusMatricula(boolean matriculaEstaAtiva) {
        this.possuiMatriculaAtiva = matriculaEstaAtiva;
    }

    public EstadoCivil obterEstadoCivil() {
        return estadoCivilAtual;
    }

    public void definirEstadoCivil(EstadoCivil novoEstadoCivil) {
        this.estadoCivilAtual = novoEstadoCivil;
    }
    
    // Método de validação
    private void validarIdadeNaoNegativa(int idade) {
        if (idade < 0) {
            throw new IllegalArgumentException("Idade não pode ser negativa");
        }
    }

    // Getters e setters para curso
    public Curso obterCursoMatriculado() {
        return cursoMatriculado;
    }

    public void definirCursoMatriculado(Curso novoCurso) {
        this.cursoMatriculado = novoCurso;
    }

    // Getters e setters para contato
    public List<String> obterTelefonesContato() {
        return criarListaSeguraDeTelefones(numerosTelefone);
    }

    public void definirTelefonesContato(List<String> novosTelefones) {
        this.numerosTelefone = criarListaSeguraDeTelefones(novosTelefones);
    }

    // Métodos de consulta e regras de negócio
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
    
    // Métodos auxiliares privados
    private List<String> criarListaSeguraDeTelefones(List<String> telefones) {
        return telefones != null ? new ArrayList<>(telefones) : new ArrayList<>();
    }

    // Sobrescrita de métodos Object para comparação e debug
    @Override
    public boolean equals(Object outroObjeto) {
        if (this == outroObjeto) return true;
        if (outroObjeto == null || getClass() != outroObjeto.getClass()) return false;
        
        Aluno outroAluno = (Aluno) outroObjeto;
        return numeroMatricula == outroAluno.numeroMatricula;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(numeroMatricula);
    }

    @Override
    public String toString() {
        return String.format("Aluno{matricula=%d, nome='%s', curso=%s, ativo=%s}", 
                numeroMatricula, 
                nomeCompleto, 
                cursoMatriculado != null ? cursoMatriculado.toString() : "Sem curso",
                possuiMatriculaAtiva ? "Sim" : "Não");
    }
}
