package br.edu.ibmec.resource;

/**
 * Aplicação com serviços REST para gestão de cursos.
 *
 * @author  Thiago Silva de Souza
 * @version 1.0
 * @since   2012-02-29
 */


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

import br.edu.ibmec.service.InscricaoService;
import br.edu.ibmec.dto.InscricaoDTO;
import br.edu.ibmec.dto.TurmaDTO;
import br.edu.ibmec.exception.DaoException;
import br.edu.ibmec.exception.ServiceException;
import br.edu.ibmec.exception.ServiceException.ServiceExceptionEnum;

@Path("inscricao")
@Consumes("application/xml")
@Produces("application/xml")
public class InscricaoResource {

    private InscricaoService inscricaoService;

    public InscricaoResource() {
        this.inscricaoService = new InscricaoService();
    }

    @GET
    @Path("{matricula}/{codigo}/{ano}/{semestre}")
    @Produces( { "application/xml", "application/json"})
    public Response buscarInscricao(@PathParam("matricula") String matricula, @PathParam("codigo") String codigo,
                                    @PathParam("ano") String ano, @PathParam("semestre") String semestre) {
        try {
            InscricaoDTO inscricaoDTO = inscricaoService.buscarInscricao(new Integer(matricula).intValue(), new Integer(codigo).intValue(),
                    new Integer(ano).intValue(), new Integer(semestre).intValue());
            Response resposta = Response.ok(inscricaoDTO).build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }

    @POST
    public Response cadastrarInscricao(InscricaoDTO inscricaoDTO) throws ServiceException,
            DaoException {
        try {
            inscricaoService.cadastrarInscricao(inscricaoDTO);
            return Response.created(
                    new URI("" + inscricaoDTO.getAluno() + "/" + inscricaoDTO.getCodigo() + "/" + inscricaoDTO.getAno() + "/"
                            + inscricaoDTO.getSemestre())).build();
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
    public Response alterarTurma(InscricaoDTO inscricaoDTO) {
        try {
            inscricaoService.alterarInscricao(inscricaoDTO);
            return Response.created(
                    new URI("" + inscricaoDTO.getAluno() + "/" + inscricaoDTO.getCodigo() + "/" + inscricaoDTO.getAno() + "/"
                            + inscricaoDTO.getSemestre())).build();
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
    @Path("{matricula}/{codigo}/{ano}/{semestre}")
    public Response removerInscricao(@PathParam("matricula") String matricula, @PathParam("codigo") String codigo,
                                     @PathParam("ano") String ano, @PathParam("semestre") String semestre) {
        try {
            inscricaoService.removerInscricao(new Integer(matricula).intValue(), new Integer(codigo).intValue(),
                    new Integer(ano).intValue(), new Integer(semestre).intValue());
            Response resposta = Response.ok().build();
            return resposta;
        } catch (DaoException e) {
            return Response.status(404).build();
        }
    }
}