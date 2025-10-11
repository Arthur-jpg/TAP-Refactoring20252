package br.edu.ibmec.dto;

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
    private int codigo;
    private String nome;
    private int curso;

    /*
     * private List<TurmaDTO> turmas = new ArrayList<TurmaDTO>(); private
     * List<AlunoMonitor> monitores = new ArrayList<AlunoMonitor>();
     */

}