package br.edu.ibmec.service.calculo.impl;

import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.calculo.CalculoNotaResultado;
import br.edu.ibmec.service.calculo.CalculoNotaStrategy;
import br.edu.ibmec.service.calculo.TipoCalculoNota;
import org.springframework.stereotype.Component;

@Component
public class MediaComRecuperacaoCalculoNotaStrategy implements CalculoNotaStrategy {

    @Override
    public TipoCalculoNota getTipo() {
        return TipoCalculoNota.MEDIA_COM_RECUPERACAO;
    }

    @Override
    public CalculoNotaResultado calcular(Inscricao inscricao) throws ServiceException {
        Double n1 = inscricao.getNotaAv1();
        Double n2 = inscricao.getNotaAv2();
        if (n1 == null || n2 == null) {
            throw new ServiceException("Notas 1 e 2 são obrigatórias para cálculo com recuperação");
        }
        double base = (n1 + n2) / 2.0;
        if (base >= 6) {
            return new CalculoNotaResultado(base, "APROVADO");
        }
        Double recuperacao = inscricao.getNotaRecuperacao();
        if (recuperacao == null) {
            throw new ServiceException("Nota de recuperação é obrigatória quando a média inicial é inferior a 6");
        }
        double mediaFinal = (base + recuperacao) / 2.0;
        String status = mediaFinal >= 6 ? "APROVADO" : "REPROVADO";
        return new CalculoNotaResultado(mediaFinal, status);
    }
}
