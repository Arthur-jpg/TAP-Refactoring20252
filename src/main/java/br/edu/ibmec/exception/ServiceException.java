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
        this.message = (tipo != null) ? tipo.getDescricao() : null;
    }

    public ServiceException(ArrayList<String> listaErrosCurso) {
        this.message = String.join(", ", listaErrosCurso);
    }

    public String getMessage() {
        if (message != null && !message.isEmpty()) {
            return message;
        }
        if (tipo != null && tipo.getDescricao() != null && !tipo.getDescricao().isEmpty()) {
            return tipo.getDescricao();
        }
        return super.getMessage();
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