package br.edu.ibmec.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe de identificação composta para a entidade Turma.
 * Implementa Serializable conforme requerido pela JPA para chaves compostas.
 */
public class TurmaId implements Serializable {
    private int codigo;
    private int ano;
    private int semestre;

    public TurmaId() {
    }

    public TurmaId(int codigo, int ano, int semestre) {
        this.codigo = codigo;
        this.ano = ano;
        this.semestre = semestre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TurmaId)) return false;
        TurmaId turmaId = (TurmaId) o;
        return codigo == turmaId.codigo && 
               ano == turmaId.ano && 
               semestre == turmaId.semestre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, ano, semestre);
    }

    @Override
    public String toString() {
        return "TurmaId{" +
                "codigo=" + codigo +
                ", ano=" + ano +
                ", semestre=" + semestre +
                '}';
    }
}