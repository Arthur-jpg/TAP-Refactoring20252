package br.edu.ibmec.service.calculo.impl;

import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.service.calculo.CalculoNotaResultado;
import br.edu.ibmec.service.calculo.CalculoNotaStrategy;
import br.edu.ibmec.service.calculo.TipoCalculoNota;
import org.springframework.stereotype.Component;

@Component
public class MediaSimplesCalculoNotaStrategy implements CalculoNotaStrategy {

    @Override
    public TipoCalculoNota getTipo() {
        return TipoCalculoNota.MEDIA_SIMPLES;
    }

    @Override
    public CalculoNotaResultado calcular(Inscricao inscricao) throws ServiceException {
        Double n1 = inscricao.getNotaAv1();
        Double n2 = inscricao.getNotaAv2();
        if (n1 == null || n2 == null) {
            throw new ServiceException("Notas 1 e 2 são obrigatórias para cálculo da média");
        }
        double media = (n1 + n2) / 2.0;
        String status = media >= 6 ? "APROVADO" : "REPROVADO";
        return new CalculoNotaResultado(media, status);
    }
}
