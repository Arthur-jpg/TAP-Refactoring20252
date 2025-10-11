package br.edu.ibmec.dto;

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
    private float avaliacao1;
    private float avaliacao2;
    private float media;
    private int numFaltas;
    private String situacao;

    private int aluno;
    private int codigo;
    private int ano;
    private int semestre;

    // private Avaliacao avaliacao;
}