package br.edu.ibmec.service.validation;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.exception.ServiceException;

/**
 * Estratégia do padrão Strategy utilizada para encapsular diferentes regras
 * de validação de inscrições. Cada implementação decide o que validar e o
 * serviço apenas executa a lista de estratégias configuradas no contexto Spring.
 */
public interface InscricaoValidacaoStrategy {

    void validar(InscricaoDTO dto) throws ServiceException;
}
