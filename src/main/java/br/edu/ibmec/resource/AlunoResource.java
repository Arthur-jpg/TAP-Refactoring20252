package br.edu.ibmec.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import service.AlunoService;
import dto.AlunoDTO;
import entity.Aluno;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

@Path("aluno")
@Consumes("application/xml")
@Produces("application/xml")
public class AlunoResource {

    private AlunoService alunoService;

    public AlunoResource() {
        this.alunoService = new AlunoService();
    }

    @GET
    // @Produces(MediaType.APPLICATION_JSON + ", " + MediaType.TEXT_PLAIN)
    // @Produces({"application/json", "text/plain"})
    // @Produces("application/json")
    @Produces( { "application/xml", "application/json"})
    @Path("{matricula}")
    public Response buscarAluno(@PathParam("matricula") String matricula) {
        try {
            AlunoDTO alunoDTO = alunoService.buscarAluno(new Integer(matricula)
                    .intValue());
            Response resposta = Response.ok(alunoDTO).build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @POST
    public Response cadastrarAluno(AlunoDTO alunoDTO) throws ServiceException,
            DaoException {
        try {
            alunoService.cadastrarAluno(alunoDTO);
            return Response.created(new URI("" + alunoDTO.getMatricula())).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO)
                return Response.status(400).header("Motivo", "C�digo inv�lido")
                        .build();
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO)
                return Response.status(400).header("Motivo", "Nome inv�lido")
                        .build();
            else
                return Response.status(400).header("Motivo", e.getMessage())
                        .build();
        } catch (DaoException e) {
            return Response.status(400).header("Motivo",
                    "Erro no banco de dados").build();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    @PUT
    public Response alterarAluno(AlunoDTO alunoDTO) {
        try {
            alunoService.alterarAluno(alunoDTO);
            return Response.created(new URI("" + alunoDTO.getMatricula())).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO)
                return Response.status(400).header("Motivo", "C�digo inv�lido")
                        .build();
            if (e.getTipo() == ServiceExceptionEnum.CURSO_NOME_INVALIDO)
                return Response.status(400).header("Motivo", "Nome inv�lido")
                        .build();
            else
                return Response.status(400).header("Motivo", e.getMessage())
                        .build();
        } catch (DaoException e) {
            return Response.status(400).header("Motivo",
                    "Erro no banco de dados").build();
        } catch (URISyntaxException e) {
            throw new RuntimeException();
        }
    }

    @DELETE
    @Path("{matricula}")
    public Response removerAluno(@PathParam("matricula") String matricula) {
        try {
            alunoService.removerAluno(new Integer(matricula)
                    .intValue());
            Response resposta = Response.ok().build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Produces("text/plain")
    public String listarAlunos() {
        List<String> nomes = new ArrayList<String>();
        for(Iterator<Aluno> it = alunoService.listarAlunos().iterator(); it.hasNext();)
        {
            Aluno aluno = (Aluno)it.next();
            nomes.add(aluno.getNome());
        } return nomes.toString();
    }
}