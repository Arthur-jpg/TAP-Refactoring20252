package br.edu.ibmec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Curso
 * Contém validações Bean Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {
    
    @Min(value = 1, message = "Código do curso deve ser um número positivo")
    private int codigo;
    
    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(max = 100, message = "Nome do curso deve ter no máximo 100 caracteres")
    private String nome;

    // private List<AlunoDTO> alunos = new ArrayList<AlunoDTO>();
    // private List<DisciplinaDTO> disciplinas = new ArrayList<DisciplinaDTO>();
}