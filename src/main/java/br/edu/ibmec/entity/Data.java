package br.edu.ibmec.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    @Column(name = "dia_nascimento")
    private int dia;
    
    @Column(name = "mes_nascimento") 
    private int mes;
    
    @Column(name = "ano_nascimento")
    private int ano;

    @Override
    public String toString() {
        return ""+dia+"/"+mes+"/"+ano;
    }

    /**
     * Cria uma Data a partir de uma string no formato "dd/MM/yyyy"
     */
    public static Data fromString(String dataStr) {
        if (dataStr == null || dataStr.trim().isEmpty()) {
            return null;
        }
        
        String[] partes = dataStr.trim().split("/");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy");
        }
        
        try {
            int dia = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);
            int ano = Integer.parseInt(partes[2]);
            return new Data(dia, mes, ano);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use dd/MM/yyyy", e);
        }
    }
}