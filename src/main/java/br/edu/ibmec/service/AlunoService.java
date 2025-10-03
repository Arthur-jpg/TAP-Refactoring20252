package br.edu.ibmec.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import br.edu.ibmec.dao.EscolaDAO;
import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Data;
import br.edu.ibmec.entity.EstadoCivil;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

public class AlunoService {
    private EscolaDAO dao;

    public AlunoService() {
        this.dao = EscolaDAO.getInstance();
    }

    public static final Data getData(String data)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataConvertida = null;
        try {
            dataConvertida = sdf.parse(data);
            Data dataRetorno = new Data();
            dataRetorno.setAno(dataConvertida.getYear());
            dataRetorno.setMes(dataConvertida.getMonth());
            dataRetorno.setDia(dataConvertida.getDay());
            return dataRetorno;
        } catch (Exception e) {
            System.out.println("Erro Convers�o da data: " + e.getMessage());
            return null;
        }
    }

    public AlunoDTO buscarAluno(int matricula) throws DaoException {
        try {
            AlunoDTO alunoDTO = new AlunoDTO(dao.getAluno(matricula)
                    .getMatricula(), dao.getAluno(matricula).getNome(),
                    dao.getAluno(matricula).getDataNascimento().toString(),
                    dao.getAluno(matricula).isMatriculaAtiva(),
                    null,
                    dao.getAluno(matricula).getCurso().getCodigo(),
                    dao.getAluno(matricula).getTelefones());
            return alunoDTO;
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }

    public Collection<Aluno> listarAlunos() {
        return dao.getAlunos();
    }

    public void cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Aluno aluno = new Aluno(alunoDTO.getMatricula(), alunoDTO.getNome(),
                getData(alunoDTO.getDtNascimento().toString()), alunoDTO.isMatriculaAtiva(),
                EstadoCivil.solteiro, dao.getCurso(alunoDTO.getCurso()),
                alunoDTO.getTelefones());

        try {
            dao.addAluno(aluno);
            Curso curso = dao.getCurso(alunoDTO.getCurso());
            curso.getAlunos().add(aluno);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 99)) {
            throw new ServiceException(
                    ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((alunoDTO.getNome().length() < 1)
                || (alunoDTO.getNome().length() > 20)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }

        Aluno aluno = new Aluno(alunoDTO.getMatricula(), alunoDTO.getNome(),
                getData(alunoDTO.getDtNascimento()), alunoDTO.isMatriculaAtiva(),
                EstadoCivil.solteiro, dao.getCurso(alunoDTO.getCurso()),
                alunoDTO.getTelefones());

        try {
            dao.updateAluno(aluno);
        } catch (DaoException e) {
            throw new DaoException("erro do dao no service throw");
        }
    }

    public void removerAluno(int matricula) throws DaoException {
        try {
            dao.removeAluno(matricula);
        } catch (DaoException e) {
            throw new DaoException("");
        }
    }
}