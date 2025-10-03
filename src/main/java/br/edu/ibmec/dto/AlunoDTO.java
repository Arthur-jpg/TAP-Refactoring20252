package br.edu.ibmec.dto;

/**
 * Aplicação com serviços REST para gestão de cursos.
 *
 * @author  Thiago Silva de Souza
 * @version 1.0
 * @since   2012-02-29
 */


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="aluno")
public class AlunoDTO {
    private int matricula;
    private String nome;
    private String dtNascimento;
    private int idade;
    private boolean matriculaAtiva;
    private EstadoCivilDTO estadoCivilDTO;
    private Vector<String> telefones;

    private int curso;

    //private List<InscricaoDTO> inscricoes = new ArrayList<InscricaoDTO>();

    public AlunoDTO() {

    }

    public AlunoDTO(int matricula,
                    String nome,
                    String dtNascimento,
                    boolean matriculaAtiva,
                    EstadoCivilDTO estadoCivilDTO,
                    int curso,
                    Vector<String> telefones) {
        this.matricula = matricula;
        this.nome = nome;
        this.dtNascimento = dtNascimento;
        this.matriculaAtiva = matriculaAtiva;
        this.estadoCivilDTO = estadoCivilDTO;
        this.curso = curso;

        this.idade = getIdadeConvertida(dtNascimento);
        this.telefones = telefones;
    }

    private int getIdadeConvertida(String data)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataConvertida = null;
        try {
            dataConvertida = sdf.parse(data);
            Date hoje = new Date();
            return hoje.getYear() - dataConvertida.getYear();
        } catch (Exception e) {
            System.out.println("Erro Convers�o da idade: " + e.getMessage());
            return 0;
        }
    }


    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public boolean isMatriculaAtiva() {
        return matriculaAtiva;
    }

    public void setMatriculaAtiva(boolean matriculaAtiva) {
        this.matriculaAtiva = matriculaAtiva;
    }

    public EstadoCivilDTO getEstadoCivil() {
        return estadoCivilDTO;
    }

    public void setEstadoCivil(EstadoCivilDTO estadoCivilDTO) {
        this.estadoCivilDTO = estadoCivilDTO;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Vector<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Vector<String> telefones) {
        this.telefones = telefones;
    }

}