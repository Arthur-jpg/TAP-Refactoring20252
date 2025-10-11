package br.edu.ibmec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
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
    @Max(value = 99, message = "Código do curso deve estar entre 1 e 99")
    private int codigo;
    
    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(max = 20, message = "Nome do curso deve ter no máximo 20 caracteres")
    private String nome;

}