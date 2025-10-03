package br.edu.ibmec.dto;


import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum EstadoCivilDTO {
    solteiro, casado, divorciado, viuvo;
}