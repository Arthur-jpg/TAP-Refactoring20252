package br.edu.ibmec.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoDTO {

    @NotNull
    @Min(1)
    private Long id;

    @Min(1)
    private int alunoMatricula;

    @Min(1)
    private int turmaCodigo;

    @Min(1900)
    @Max(2100)
    private int ano;

    @Min(1)
    @Max(2)
    private int semestre;
}
