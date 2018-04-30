package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import auxiliary.RFC1;
import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.Reserva;

@Path("operadores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class OperadorService {

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
	public Response postReserva(Reserva reserva) {
		return null;
	}
	
	@GET
	@Path("dineroanioactual")
	public Response getDineroAnioActual(@QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		System.out.println("ENTRO dinANACT");
		
		//-1 corresponde al token del admin
		//if(token == -1) {
			System.out.println("TOKEN ADMIN");
			try {
				List<RFC1> l = tm.dineroRecibidoAnioActual(token);
				return Response.status(200).entity(l).build();
			}
			catch(BusinessLogicException e) {
				return Response.status(400).entity(doErrorMessage(e)).build();
			}
			catch(Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		//}
		/*else {
			try{
				Double d = tm.dineroRecibidoAnioActualOp(token);
				return Response.status(200).entity(d).build();
			}
			catch(BusinessLogicException e) {
				return Response.status(400).entity(doErrorMessage(e)).build();
			}
			catch(Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		}*/
	}

	@GET
	@Path("dineroaniocorrido")
	public Response getDineroAnioCorrido(@QueryParam("token")Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		//-1 corresponde al token del admin
		
			try {
				List<RFC1> l = tm.dineroRecibidoAnioCorrido(token);
				return Response.status(200).entity(l).build();
			}
			catch(BusinessLogicException e) {
				return Response.status(400).entity(doErrorMessage(e)).build();
			}
			catch(Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		
		/*else {
			try {
				Double d = tm.dineroRecibidoAnioCorridoOp(token);
				return Response.status(200).entity(d).build();
			}
			catch(BusinessLogicException e) {
				return Response.status(400).entity(doErrorMessage(e)).build();
			}
			catch(Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		}*/
	}
}
