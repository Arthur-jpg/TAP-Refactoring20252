package br.edu.ibmec.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para transferência de dados de Aluno
 * Contém validações Bean Validation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {
    
    @Min(value = 1, message = "Matrícula deve ser um número positivo")
    @Max(value = 99, message = "Matrícula deve estar entre 1 e 99")
    private int matricula;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 20, message = "Nome deve ter no máximo 20 caracteres")
    private String nome;
    
    private String dtNascimento;
    
    @Min(value = 0, message = "Idade deve ser positiva")
    private int idade;
    
    private boolean matriculaAtiva;
    
    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    private EstadoCivilDTO estadoCivilDTO;
    
    @Builder.Default
    private List<String> telefones = new ArrayList<>();

    private int curso;

    // Mantém API existente: getEstadoCivil()/setEstadoCivil() ao invés de getEstadoCivilDTO()
    public EstadoCivilDTO getEstadoCivil() {
        return this.estadoCivilDTO;
    }

    public void setEstadoCivil(EstadoCivilDTO estadoCivilDTO) {
        this.estadoCivilDTO = estadoCivilDTO;
    }
}