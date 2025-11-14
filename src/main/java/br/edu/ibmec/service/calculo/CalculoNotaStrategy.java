package br.edu.ibmec.service.calculo;

import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;

public interface CalculoNotaStrategy {

    TipoCalculoNota getTipo();

    CalculoNotaResultado calcular(Inscricao inscricao) throws ServiceException;
}
