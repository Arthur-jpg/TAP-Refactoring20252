package br.edu.ibmec.dto;

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
    private int codigo;
    private int ano;
    private int semestre;
    private int disciplina;

}