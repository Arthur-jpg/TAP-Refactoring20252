package br.edu.ibmec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avaliacao {
    private long id;
    private float avaliacao1;
    private float avaliacao2;
    private float media;
    private int numFaltas;
    private String situacao;

    private Inscricao inscricao;

    public Avaliacao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }
}