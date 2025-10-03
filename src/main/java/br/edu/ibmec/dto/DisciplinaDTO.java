package br.edu.ibmec.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="disciplina")
public class DisciplinaDTO {
    private int codigo;
    private String nome;
    private int curso;

    /*
     * private List<TurmaDTO> turmas = new ArrayList<TurmaDTO>(); private
     * List<AlunoMonitor> monitores = new ArrayList<AlunoMonitor>();
     */

    public DisciplinaDTO() {

    }

    public DisciplinaDTO(int codigo, String nome, int curso) {
        this.codigo = codigo;
        this.nome = nome;
        this.curso = curso;
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

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + codigo;
        result = prime * result + curso;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DisciplinaDTO other = (DisciplinaDTO) obj;
        if (codigo != other.codigo)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DisciplinaDTO [codigo=" + codigo + ", curso=" + curso
                + ", nome=" + nome + "]";
    }



}