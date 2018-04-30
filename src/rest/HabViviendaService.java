package rest;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.HabVivienda;

@Path("habsviviendas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HabViviendaService {

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
	// ----------------------------

	@POST
	public Response postHabVivienda(HabVivienda hv, @QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			tm.registrarHabitacionVivienda(hv, token);
			return Response.status(200).entity(hv).build();
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
}
