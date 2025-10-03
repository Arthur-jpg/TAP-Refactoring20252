package br.edu.ibmec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Data {
    @Column(name = "dia_nascimento")
    private int dia;
    
    @Column(name = "mes_nascimento") 
    private int mes;
    
    @Column(name = "ano_nascimento")
    private int ano;

    public Data() {

    }

    public Data(int dia, int mes, int ano) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return ""+dia+"/"+mes+"/"+ano;
    }
}