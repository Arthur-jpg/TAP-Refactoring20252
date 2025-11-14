package br.edu.ibmec.service.mensalidade.impl;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.mensalidade.CalculoMensalidadeStrategy;
import br.edu.ibmec.service.mensalidade.TipoCalculoMensalidade;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MensalidadePadraoStrategy implements CalculoMensalidadeStrategy {

    @Override
    public TipoCalculoMensalidade getTipo() {
        return TipoCalculoMensalidade.PADRAO;
    }

    @Override
    public BigDecimal calcular(Aluno aluno, List<Inscricao> inscricoes) throws ServiceException {
        if (inscricoes == null || inscricoes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Inscricao inscricao : inscricoes) {
            if (inscricao.getTurma() == null || inscricao.getTurma().getDisciplina() == null
                    || inscricao.getTurma().getDisciplina().getValorBase() == null) {
                throw new ServiceException("Disciplina sem valor base configurado para cálculo de mensalidade");
            }
            total = total.add(inscricao.getTurma().getDisciplina().getValorBase());
        }
        return total;
    }
}

