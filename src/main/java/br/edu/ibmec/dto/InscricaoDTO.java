package br.edu.ibmec.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name="inscricao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscricaoDTO {
    @Min(0)
    @Max(10)
    private float avaliacao1;
    
    @Min(0)
    @Max(10)
    private float avaliacao2;
    
    private float media;
    
    @Min(0)
    private int numFaltas;
    
    private String situacao;

    @Min(1)
    private int aluno;
    
    @Min(1)
    @Max(999)
    private int codigo;
    
    @Min(1900)
    @Max(2020)
    private int ano;
    
    @Min(1)
    @Max(2)
    private int semestre;
}