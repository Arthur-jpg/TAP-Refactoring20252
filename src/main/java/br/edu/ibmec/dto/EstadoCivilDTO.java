package br.edu.ibmec.dto;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum EstadoCivilDTO {
    solteiro, casado, divorciado, viuvo;
}