package rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.Reserva;
import vos.ReservaColectiva;

@Path("reservascolectivas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservaColectivaService {

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la
	 * conexion actual.
	 */
	@Context
	private ServletContext context;

	// ----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	// ----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy
	 * actual dentro del servidor.
	 * 
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e) {
		return "{ \"ERROR\": \"" + e.getMessage() + "\"}";
	}

	// ----------------------------
	// SERVICIOS
	// ---------------------------
	@POST
	public Response postReservaColectiva(ReservaColectiva reserva, @QueryParam("necesitadas") int necesitadas, @QueryParam("token") Long token, @QueryParam("tipo") String tipo) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		corregirDesfase(reserva);
		
		try {
			tm.addReservaColectiva(reserva, necesitadas, token, tipo);
			return Response.status(200).entity(reserva).build();
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@PUT
	public Response deleteReservaColectiva(ReservaColectiva reserva, @QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		reserva.getFechaInicio().setDate(reserva.getFechaInicio().getDate()+1);
		reserva.getFechaFin().setDate(reserva.getFechaFin().getDate()+1);
		
		try {
			Double reservaCancelada = tm.cancelarReservaColectiva(reserva, token);
			return Response.status(200).entity(reservaCancelada).build();
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void corregirDesfase(ReservaColectiva reserva) {

		//Para corregir el desfase de fechas.
		reserva.getFechaFin().setDate(reserva.getFechaFin().getDate()+1);
		reserva.getFechaInicio().setDate(reserva.getFechaInicio().getDate()+1);
	}
}
