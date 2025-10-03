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

import service.CursoService;
import dto.CursoDTO;
import entity.Curso;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

@Path("curso")
@Consumes("application/xml")
@Produces("application/xml")
public class CursoResource {

    private CursoService cursoService;

    public CursoResource() {
        this.cursoService = new CursoService();
    }

    @GET
    // @Produces(MediaType.APPLICATION_JSON + ", " + MediaType.TEXT_PLAIN)
    // @Produces({"application/json", "text/plain"})
    // @Produces("application/json")
    @Produces( { "application/xml", "application/json"})
    @Path("{codigo}")
    public Response buscarCurso(@PathParam("codigo") String codigo) {
        try {
            CursoDTO cursoDTO = cursoService.buscarCurso(new Integer(codigo)
                    .intValue());
            Response resposta = Response.ok(cursoDTO).build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @POST
    public Response cadastrarCurso(CursoDTO cursoDTO) throws ServiceException,
            DaoException {
        try {
            cursoService.cadastrarCurso(cursoDTO);
            return Response.created(new URI("" + cursoDTO.getCodigo())).build();
        } catch (ServiceException e) {
            if (e.getTipo() == ServiceExceptionEnum.CURSO_CODIGO_INVALIDO)
                return Response.status(400).header("Motivo", e.getTipo())
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
    public Response alterarCurso(CursoDTO cursoDTO) {
        try {
            cursoService.alterarCurso(cursoDTO);
            return Response.created(new URI("" + cursoDTO.getCodigo())).build();
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
    @Path("{codigo}")
    public Response removerCurso(@PathParam("codigo") String codigo) {
        try {
            cursoService.removerCurso(new Integer(codigo)
                    .intValue());
            Response resposta = Response.ok().build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @GET
    @Produces("text/plain")
    public String listarCursos() {
        List<String> nomes = new ArrayList<String>();
        for (Iterator<Curso> it = cursoService.listarCursos().iterator(); it
                .hasNext();) {
            Curso curso = (Curso) it.next();
            nomes.add(curso.getNome());
        }
        return nomes.toString();
    }
}