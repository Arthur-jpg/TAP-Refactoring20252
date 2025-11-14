package br.edu.ibmec.service;

import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.repository.AlunoRepository;
import br.edu.ibmec.repository.InscricaoRepository;
import br.edu.ibmec.service.mensalidade.CalculoMensalidadeStrategy;
import br.edu.ibmec.service.mensalidade.TipoCalculoMensalidade;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MensalidadeService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired(required = false)
    private List<CalculoMensalidadeStrategy> estrategias;

    private Map<TipoCalculoMensalidade, CalculoMensalidadeStrategy> estrategiaPorTipo;

    @PostConstruct
    void init() {
        if (estrategias != null) {
            estrategiaPorTipo = estrategias.stream()
                    .collect(Collectors.toMap(CalculoMensalidadeStrategy::getTipo, e -> e));
        }
    }

    public BigDecimal calcularMensalidade(int matricula, TipoCalculoMensalidade tipo)
            throws DaoException, ServiceException {

        Aluno aluno = alunoRepository.findById(matricula)
                .orElseThrow(() -> new DaoException("Aluno com matrícula " + matricula + " não encontrado"));

        List<Inscricao> inscricoes = inscricaoRepository.findByAlunoMatricula(matricula);

        if (tipo == null) {
            tipo = TipoCalculoMensalidade.PADRAO;
        }
        if (estrategiaPorTipo == null || estrategiaPorTipo.isEmpty()) {
            throw new ServiceException("Nenhuma estratégia de cálculo de mensalidade configurada");
        }
        CalculoMensalidadeStrategy strategy = Optional.ofNullable(estrategiaPorTipo.get(tipo))
                .orElse(estrategiaPorTipo.get(TipoCalculoMensalidade.PADRAO));
        if (strategy == null) {
            throw new ServiceException("Estratégia de cálculo de mensalidade não encontrada");
        }
        return strategy.calcular(aluno, inscricoes);
    }
}

