package br.edu.ibmec.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplinaDTO {

    @Min(1)
    private int codigo;

    @NotBlank
    @Size(max = 80)
    private String nome;

    @Min(1)
    private int curso;

}
