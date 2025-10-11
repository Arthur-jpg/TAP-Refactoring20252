package br.edu.ibmec.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name="turma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurmaDTO {
    @Min(1)
    @Max(99)
    private int codigo;
    
    @Min(1900)
    @Max(2020)
    private int ano;
    
    @Min(1)
    @Max(2)
    private int semestre;
    
    @Min(1)
    private int disciplina;
}