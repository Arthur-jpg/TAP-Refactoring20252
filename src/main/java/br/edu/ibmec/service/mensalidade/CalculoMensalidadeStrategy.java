package br.edu.ibmec.service.mensalidade;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;
import java.math.BigDecimal;
import java.util.List;

public interface CalculoMensalidadeStrategy {

    TipoCalculoMensalidade getTipo();

    BigDecimal calcular(Aluno aluno, List<Inscricao> inscricoes) throws ServiceException;
}

