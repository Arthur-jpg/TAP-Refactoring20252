package br.edu.ibmec.resource;


import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import service.TurmaService;
import dto.TurmaDTO;
import exception.DaoException;
import exception.ServiceException;
import exception.ServiceException.ServiceExceptionEnum;

@Path("turma")
@Consumes("application/xml")
@Produces("application/xml")
public class TurmaResource {

    private TurmaService turmaService;

    public TurmaResource() {
        this.turmaService = new TurmaService();
    }

    @GET
    @Path("{codigo}/{ano}/{semestre}")
    @Produces( { "application/xml", "application/json"})
    public Response buscarTurma(@PathParam("codigo") String codigo,
                                @PathParam("ano") String ano, @PathParam("semestre") String semestre) {
        try {
            TurmaDTO turmaDTO = turmaService.buscarTurma(new Integer(codigo).intValue(),
                    new Integer(ano).intValue(), new Integer(semestre).intValue());
            Response resposta = Response.ok(turmaDTO).build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @POST
    public Response cadastrarTurma(TurmaDTO turmaDTO) throws ServiceException,
            DaoException {
        try {
            turmaService.cadastrarTurma(turmaDTO);
            return Response.created(
                    new URI("" + turmaDTO.getCodigo() + "/" + turmaDTO.getAno() + "/"
                            + turmaDTO.getSemestre())).build();
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
    public Response alterarTurma(TurmaDTO turmaDTO) {
        try {
            turmaService.cadastrarTurma(turmaDTO);
            return Response.created(
                    new URI("" + turmaDTO.getCodigo() + "/" + turmaDTO.getAno() + "/"
                            + turmaDTO.getSemestre())).build();
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
    @Path("{codigo}/{ano}/{semestre}")
    public Response removerTurma(@PathParam("codigo") String codigo,
                                 @PathParam("ano") String ano, @PathParam("semestre") String semestre) {
        try {
            turmaService.removerTurma(new Integer(codigo).intValue(),
                    new Integer(ano).intValue(), new Integer(semestre).intValue());
            Response resposta = Response.ok().build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

/*	@GET
	@Path("{codigo}/{ano}/{semestre}")
	public Response buscarTurma(@PathParam("codigo") String codigo,
			@PathParam("ano") String ano, @PathParam("semestre") String semestre) {
		TurmaDTO turma = escolaService.buscarTurma(new Integer(codigo).intValue(),
				new Integer(ano).intValue(), new Integer(semestre).intValue());
		if (turma == null) {
			return Response.status(HttpServletResponse.SC_NOT_FOUND).build();
		} else {
			Response resposta = Response.ok(turma).build();
			return resposta;
		}
	}*/

/*	@PUT
	public Response alterarTurma(TurmaDTO turma) {
		turma = escolaService.alterarTurma(turma);
		try {
			return Response.created(
					new URI("" + turma.getCodigo() + "/" + turma.getAno() + "/"
							+ turma.getSemestre())).build();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		// return Response.status(400).build();
	}*/
}