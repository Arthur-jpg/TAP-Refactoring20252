package br.edu.ibmec.exception;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class ServiceException extends Exception {
    private String message;
    private ServiceExceptionEnum tipo;

    // private ArrayList;

    public ServiceException() {

    }

    public ServiceException(String msg) {
        super(msg);
        this.message = msg;
    }

    public ServiceException(ServiceExceptionEnum tipo) {
        this.tipo = tipo;
    }

    public ServiceException(ArrayList<String> listaErrosCurso) {
        this.message = String.join(", ", listaErrosCurso);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public ServiceExceptionEnum getTipo() {
        return tipo;
    }

    public void setTipo(ServiceExceptionEnum tipo) {
        this.tipo = tipo;
    }

    public enum ServiceExceptionEnum {

        CURSO_CODIGO_INVALIDO("Código de curso inválido"), CURSO_NOME_INVALIDO(
                "Nome de curso inválido"), CURSO_CODIGO_DUPLICADO("Código de curso já existe"), ALUNO_MATRICULA_INVALIDA, ALUNO_NOME_INVALIDO;

        private String descricao;
        
        private ServiceExceptionEnum() {
            this.descricao = "";
        }

        private ServiceExceptionEnum(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }

    }
}