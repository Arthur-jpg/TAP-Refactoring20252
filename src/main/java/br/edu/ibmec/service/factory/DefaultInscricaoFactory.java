package br.edu.ibmec.service.factory;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Turma;
import org.springframework.stereotype.Component;

/**
 * Implementação padrão da factory de Inscrições.
 */
@Component
public class DefaultInscricaoFactory implements InscricaoFactory {

    @Override
    public Inscricao criarInscricao(InscricaoDTO dto, Aluno aluno, Turma turma) {
        Inscricao inscricao = new Inscricao();
        inscricao.setId(dto.getId());
        inscricao.setAluno(aluno);
        inscricao.setTurma(turma);
        return inscricao;
    }
}
