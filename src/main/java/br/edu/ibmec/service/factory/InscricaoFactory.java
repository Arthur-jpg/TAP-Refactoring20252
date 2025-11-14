package br.edu.ibmec.service.factory;

import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Inscricao;
import br.edu.ibmec.entity.Turma;

/**
 * Factory Method para encapsular a construção da entidade Inscricao.
 * Permite evoluir o processo de criação (ex.: preenchimento de dados
 * adicionais, aplicação de defaults) sem impactar os serviços.
 */
public interface InscricaoFactory {

    Inscricao criarInscricao(InscricaoDTO dto, Aluno aluno, Turma turma);
}
