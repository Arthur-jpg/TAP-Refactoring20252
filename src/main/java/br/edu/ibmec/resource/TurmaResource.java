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

import br.edu.ibmec.service.TurmaService;
import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

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
            turmaService.alterarCurso(turmaDTO);
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