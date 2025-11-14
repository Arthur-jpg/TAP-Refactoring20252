package br.edu.ibmec.service.validation;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.exception.ServiceException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Verifica campos obrigatórios básicos de Inscricao (aluno/turma).
 */
@Component
@Order(1)
public class CamposObrigatoriosInscricaoStrategy implements InscricaoValidacaoStrategy {

    @Override
    public void validar(InscricaoDTO dto) throws ServiceException {
        if (dto.getAlunoMatricula() < 1) {
            throw new ServiceException("Matrícula do aluno é obrigatória");
        }
        if (dto.getTurmaCodigo() < 1) {
            throw new ServiceException("Código da turma é obrigatório");
        }
    }
}
