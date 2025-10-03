package br.edu.ibmec.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="turma")
public class TurmaDTO {
    private int codigo;
    private int ano;
    private int semestre;
    private int disciplina;

    //private List<InscricaoDTO> inscricoes = new ArrayList<InscricaoDTO>();

    public TurmaDTO() {

    }

    public TurmaDTO(int codigo, int ano, int semestre, int disciplina) {
        this.codigo = codigo;
        this.ano = ano;
        this.semestre = semestre;
        this.disciplina = disciplina;
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

    public int getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(int disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String toString() {
        return "TurmaDTO [ano=" + ano + ", codigo=" + codigo + ", disciplina="
                + disciplina + ", semestre=" + semestre + "]";
    }

}