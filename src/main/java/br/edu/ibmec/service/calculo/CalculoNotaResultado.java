package br.edu.ibmec.service.calculo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculoNotaResultado {
    private final Double mediaFinal;
    private final String status;
}
