package br.edu.ibmec.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="curso")
public class CursoDTO {
    private int codigo;
    private String nome;

    // private List<AlunoDTO> alunos = new ArrayList<AlunoDTO>();
    // private List<DisciplinaDTO> disciplinas = new ArrayList<DisciplinaDTO>();

    public CursoDTO() {

    }

    public CursoDTO(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CursoDTO [codigo=" + codigo + ", nome=" + nome + "]";
    }



}