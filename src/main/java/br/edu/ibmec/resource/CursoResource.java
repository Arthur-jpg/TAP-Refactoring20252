package br.edu.ibmec.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import br.edu.ibmec.service.CursoService;
import br.edu.ibmec.dto.CursoDTO;
import br.edu.ibmec.entity.Curso;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

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