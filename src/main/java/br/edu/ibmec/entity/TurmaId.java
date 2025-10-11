package br.edu.ibmec.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Classe de identificação composta para a entidade Turma. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurmaId implements Serializable {
    private int codigo;
    private int ano;
    private int semestre;
}