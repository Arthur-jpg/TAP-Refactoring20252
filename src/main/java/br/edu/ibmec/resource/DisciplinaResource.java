package br.edu.ibmec.resource;

import java.net.URI;
import java.net.URISyntaxException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import br.edu.ibmec.service.DisciplinaService;
import br.edu.ibmec.dto.DisciplinaDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@Path("disciplina")
@Consumes("application/xml")
@Produces("application/xml")
public class DisciplinaResource {

    private DisciplinaService disciplinaService;

    public DisciplinaResource() {
        this.disciplinaService = new DisciplinaService();
    }

    @GET
    // @Produces(MediaType.APPLICATION_JSON + ", " + MediaType.TEXT_PLAIN)
    // @Produces({"application/json", "text/plain"})
    // @Produces("application/json")
    @Produces( { "application/xml", "application/json"})
    @Path("{codigo}")
    public Response buscarDisciplina(@PathParam("codigo") String codigo) {
        try {
            DisciplinaDTO disciplinaDTO = disciplinaService
                    .buscarDisciplina(new Integer(codigo).intValue());
            Response resposta = Response.ok(disciplinaDTO).build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @POST
    @Consumes("application/xml")
    public Response cadastrarDisciplina(DisciplinaDTO disciplinaDTO)
            throws ServiceException, DaoException {
        try {
            disciplinaService.cadastrarDisciplina(disciplinaDTO);
            return Response.created(new URI("" + disciplinaDTO.getCodigo()))
                    .build();
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
    public Response alterarDisciplina(DisciplinaDTO disciplinaDTO) {
        try {
            disciplinaService.alterarDisciplina(disciplinaDTO);
            return Response.created(new URI("" + disciplinaDTO.getCodigo()))
                    .build();
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
    public Response removerDisciplina(@PathParam("codigo") String codigo) {
        try {
            disciplinaService.removerDisciplina(new Integer(codigo).intValue());
            Response resposta = Response.ok().build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

}