package br.edu.ibmec.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de Aluno
 * Contém validações Bean Validation
 */
public class AlunoDTO {
    
    @Min(value = 1, message = "Matrícula deve ser um número positivo")
    private int matricula;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    // @NotBlank(message = "Data de nascimento é obrigatória") // Temporariamente removido para testes
    private String dtNascimento;
    
    @Min(value = 0, message = "Idade deve ser positiva")
    private int idade;
    
    private boolean matriculaAtiva;
    
    // @NotNull(message = "Estado civil é obrigatório") // Temporariamente removido para testes
    private EstadoCivilDTO estadoCivilDTO;
    
    private List<String> telefones = new ArrayList<>();

    private int curso;

    //private List<InscricaoDTO> inscricoes = new ArrayList<InscricaoDTO>();

    public AlunoDTO() {

    }

    public AlunoDTO(int matricula,
                    String nome,
                    String dtNascimento,
                    boolean matriculaAtiva,
                    EstadoCivilDTO estadoCivilDTO,
                    int curso,
                    List<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dtNascimento = dtNascimento;
        this.matriculaAtiva = matriculaAtiva;
        this.estadoCivilDTO = estadoCivilDTO;
        this.curso = curso;

        this.idade = getIdadeConvertida(dtNascimento);
        this.telefones = telefones;
    }

    private int getIdadeConvertida(String data)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataConvertida = null;
        try {
            dataConvertida = sdf.parse(data);
            Date hoje = new Date();
            return hoje.getYear() - dataConvertida.getYear();
        } catch (Exception e) {
            System.out.println("Erro Convers�o da idade: " + e.getMessage());
            return 0;
        }
    }


    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isMatriculaAtiva() {
        return matriculaAtiva;
    }

    public void setMatriculaAtiva(boolean matriculaAtiva) {
        this.matriculaAtiva = matriculaAtiva;
    }

    public EstadoCivilDTO getEstadoCivil() {
        return estadoCivilDTO;
    }

    public void setEstadoCivil(EstadoCivilDTO estadoCivilDTO) {
        this.estadoCivilDTO = estadoCivilDTO;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

}