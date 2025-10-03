package service;

import java.util.Collection;

import dao.EscolaDAO;
import dto.InscricaoDTO;
import entity.Inscricao;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

public class InscricaoService {
    private EscolaDAO dao;

    public InscricaoService() {
        this.dao = EscolaDAO.getInstance();
    }

    public InscricaoDTO buscarInscricao(int matricula, int codigo, int ano,
                                        int semestre) throws DaoException {
        try {
            Inscricao inscricao = dao.getInscricao(matricula, codigo, ano,
                    semestre);
            InscricaoDTO inscricaoDTO = new InscricaoDTO(inscricao
                    .getAvaliacao1(), inscricao.getAvaliacao2(), inscricao
                    .getNumFaltas(), inscricao.getSituacao(), inscricao
                    .getAluno().getMatricula(), inscricao.getTurma()
                    .getCodigo(), inscricao.getTurma().getAno(), inscricao
                    .getTurma().getSemestre());
            return inscricaoDTO;
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

    public Collection<Inscricao> listarInscricoes() throws DaoException {
        return dao.getInscricoes();
    }

    public void cadastrarInscricao(InscricaoDTO inscricaoDTO)
            throws ServiceException, DaoException {
        if ((inscricaoDTO.getCodigo() < 1) || (inscricaoDTO.getCodigo() > 999)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((inscricaoDTO.getAno() < 1900) || (inscricaoDTO.getAno() > 2020)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Inscricao inscricao = new Inscricao(inscricaoDTO.getAvaliacao1(),
                inscricaoDTO.getAvaliacao2(), inscricaoDTO.getNumFaltas(),
                inscricaoDTO.getSituacao(), dao.getAluno(inscricaoDTO
                .getAluno()), dao.getTurma(inscricaoDTO.getCodigo(),
                inscricaoDTO.getAno(), inscricaoDTO.getSemestre()));

        try {
            dao.addInscricao(inscricao);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void alterarInscricao(InscricaoDTO inscricaoDTO)
            throws ServiceException, DaoException {
        if ((inscricaoDTO.getCodigo() < 1) || (inscricaoDTO.getCodigo() > 999)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((inscricaoDTO.getAno() < 1900) || (inscricaoDTO.getAno() > 2020)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Inscricao inscricao = new Inscricao(inscricaoDTO.getAvaliacao1(),
                inscricaoDTO.getAvaliacao2(), inscricaoDTO.getNumFaltas(),
                inscricaoDTO.getSituacao(), dao.getAluno(inscricaoDTO
                .getAluno()), dao.getTurma(inscricaoDTO.getCodigo(),
                inscricaoDTO.getAno(), inscricaoDTO.getSemestre()));

        try {
            dao.updateInscricao(inscricao);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void removerInscricao(int matricula, int codigo, int ano,
                                 int semestre) throws DaoException {
        try {
            dao.removeInscricao(matricula, codigo, ano, semestre);
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

}