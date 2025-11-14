package br.edu.ibmec.service.mensalidade.impl;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.mensalidade.CalculoMensalidadeStrategy;
import br.edu.ibmec.service.mensalidade.TipoCalculoMensalidade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Estratégia de mensalidade com desconto progressivo simples:
 * a partir de 5 disciplinas, aplica 10% de desconto sobre o total.
 */
@Component
public class MensalidadeDescontoQuantidadeStrategy implements CalculoMensalidadeStrategy {

    private static final int LIMITE_DESCONTO = 5;
    private static final BigDecimal DESCONTO = new BigDecimal("0.10");

    @Override
    public TipoCalculoMensalidade getTipo() {
        return TipoCalculoMensalidade.DESCONTO_QUANTIDADE;
    }

    @Override
    public BigDecimal calcular(Aluno aluno, List<Inscricao> inscricoes) throws ServiceException {
        BigDecimal totalBase = new MensalidadePadraoStrategy().calcular(aluno, inscricoes);
        if (inscricoes == null || inscricoes.size() < LIMITE_DESCONTO) {
            return totalBase;
        }
        BigDecimal desconto = totalBase.multiply(DESCONTO);
        return totalBase.subtract(desconto).setScale(2, RoundingMode.HALF_UP);
    }
}

