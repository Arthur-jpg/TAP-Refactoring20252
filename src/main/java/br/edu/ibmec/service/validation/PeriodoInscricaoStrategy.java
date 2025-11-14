package br.edu.ibmec.service.validation;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.exception.ServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Validação de período letivo (ano/semestre) para inscrição.
 */
@Component
@Order(2)
public class PeriodoInscricaoStrategy implements InscricaoValidacaoStrategy {

    private static final int ANO_MINIMO = 1900;
    private static final int ANO_MAXIMO = 2100;

    @Override
    public void validar(InscricaoDTO dto) throws ServiceException {
        if (dto.getAno() < ANO_MINIMO || dto.getAno() > ANO_MAXIMO) {
            throw new ServiceException("Ano inválido");
        }
        if (dto.getSemestre() < 1 || dto.getSemestre() > 2) {
            throw new ServiceException("Semestre deve ser 1 ou 2");
        }
    }
}
