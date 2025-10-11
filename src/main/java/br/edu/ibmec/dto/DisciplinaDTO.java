package br.edu.ibmec.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@XmlRootElement(name="disciplina")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "codigo")
public class DisciplinaDTO {
    @Min(1)
    @Max(99)
    private int codigo;
    
    @NotBlank
    @Size(max = 20)
    private String nome;
    
    private int curso;
}