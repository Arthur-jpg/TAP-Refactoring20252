package br.edu.ibmec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de Curso
 * Contém validações Bean Validation
 */
public class CursoDTO {
    
    @Min(value = 1, message = "Código do curso deve ser um número positivo")
    private int codigo;
    
    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(max = 100, message = "Nome do curso deve ter no máximo 100 caracteres")
    private String nome;

    // private List<AlunoDTO> alunos = new ArrayList<AlunoDTO>();
    // private List<DisciplinaDTO> disciplinas = new ArrayList<DisciplinaDTO>();

    public CursoDTO() {

    }

    public CursoDTO(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CursoDTO [codigo=" + codigo + ", nome=" + nome + "]";
    }



}