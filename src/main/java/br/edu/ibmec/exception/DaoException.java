package br.edu.ibmec.exception;

@SuppressWarnings("serial")
public class DaoException extends Exception {
    private String msg;

    public DaoException() {

    }

    public DaoException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}