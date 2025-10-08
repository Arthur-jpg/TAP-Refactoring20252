package br.edu.ibmec.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import br.edu.ibmec.dao.AlunoDAO;
import br.edu.ibmec.dao.CursoDAO;
import br.edu.ibmec.dto.AlunoDTO;
import br.edu.ibmec.entity.Aluno;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.entity.Data;
import br.edu.ibmec.entity.EstadoCivil;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlunoService {
    
    @Autowired
    private AlunoDAO alunoDAO;
    
    @Autowired
    private CursoDAO cursoDAO;

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
            Optional<Aluno> alunoOpt = alunoDAO.findByMatricula(matricula);
            if (alunoOpt.isEmpty()) {
                throw new DaoException("Aluno com matrícula " + matricula + " não encontrado");
            }
            
            Aluno aluno = alunoOpt.get();
            AlunoDTO alunoDTO = new AlunoDTO(
                    aluno.getMatricula(), 
                    aluno.getNome(),
                    aluno.getDataNascimento().toString(),
                    aluno.isMatriculaAtiva(),
                    null,
                    aluno.getCurso() != null ? aluno.getCurso().getCodigo() : 0,
                    aluno.getTelefones());
            return alunoDTO;
        } catch (DaoException e) {
            throw new DaoException("Erro ao buscar aluno: " + e.getMessage());
        }
    }

    public Collection<Aluno> listarAlunos() throws DaoException {
        return alunoDAO.findAll();
    }

    public void cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        // Validações
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((alunoDTO.getNome().length() < 1) || (alunoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        
        // Verificar se o curso existe
        Optional<Curso> cursoOpt = cursoDAO.findByCodigo(alunoDTO.getCurso());
        if (cursoOpt.isEmpty()) {
            throw new ServiceException("Curso não encontrado");
        }

        Aluno aluno = new Aluno(
                alunoDTO.getMatricula(), 
                alunoDTO.getNome(),
                getData(alunoDTO.getDtNascimento().toString()), 
                alunoDTO.isMatriculaAtiva(),
                EstadoCivil.solteiro, 
                cursoOpt.get(),
                alunoDTO.getTelefones());

        try {
            alunoDAO.save(aluno);
        } catch (DaoException e) {
            throw new DaoException("Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    public void alterarAluno(AlunoDTO alunoDTO) throws ServiceException, DaoException {
        // Validações
        if ((alunoDTO.getMatricula() < 1) || (alunoDTO.getMatricula() > 99999)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_CODIGO_INVALIDO);
        }
        if ((alunoDTO.getNome().length() < 1) || (alunoDTO.getNome().length() > 100)) {
            throw new ServiceException(ServiceExceptionEnum.CURSO_NOME_INVALIDO);
        }
        
        // Verificar se o curso existe
        Optional<Curso> cursoOpt = cursoDAO.findByCodigo(alunoDTO.getCurso());
        if (cursoOpt.isEmpty()) {
            throw new ServiceException("Curso não encontrado");
        }

        Aluno aluno = new Aluno(
                alunoDTO.getMatricula(), 
                alunoDTO.getNome(),
                getData(alunoDTO.getDtNascimento()), 
                alunoDTO.isMatriculaAtiva(),
                EstadoCivil.solteiro, 
                cursoOpt.get(),
                alunoDTO.getTelefones());

        try {
            alunoDAO.update(aluno);
        } catch (DaoException e) {
            throw new DaoException("Erro ao alterar aluno: " + e.getMessage());
        }
    }

    public void removerAluno(int matricula) throws DaoException {
        try {
            alunoDAO.deleteByMatricula(matricula);
        } catch (DaoException e) {
            throw new DaoException("Erro ao remover aluno: " + e.getMessage());
        }
    }
}