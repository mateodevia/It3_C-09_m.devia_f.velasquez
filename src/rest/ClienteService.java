package rest;

import java.sql.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.Cliente;

@Path("clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteService {


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
	public Response postCliente(Cliente c) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		corregirDesfase(c);
		
		try {
			tm.registrarCliente(c);
			return Response.status(200).entity(c).build();
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/uso")
	public Response getUsoCliente(@QueryParam("id") Long idCliente) {
		
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			return Response.status(200).entity(tm.getUsoCliente(idCliente)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
	}
	
	@GET
	@Path("/usoTipo")
	public Response getUsoTipoCliente() {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			return Response.status(200).entity(tm.getUsoTipoCliente()).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	
	@GET
	@Path("/clientesfrecuentes")
	public Response getClientesFrecuentes(@QueryParam(value = "idAl")Long idAl) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			return Response.status(200).entity(tm.getClientesFrecuentes(idAl)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/buenosclientes")
	public Response getBuenosClientes(@QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			return Response.status(200).entity(tm.darBuenosClientes(token)).build();
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/clientesreservasrango")
	public Response darClientesConReservaEnRango(@QueryParam("token") Long token, @QueryParam("alojamiento") Long alojamiento, 
			@QueryParam("fechaCotaInferior") Date fechaCotaInferior, @QueryParam("fechaCotaSuperior") Date fechaCotaSuperior,
			@QueryParam("agrupamiento") Integer agrupamiento, @QueryParam("ordenamiento")Integer ordenamiento) {
		
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			
			if(agrupamiento != null) {
				return Response.status(200).entity(tm.darClientesConReservaEnRangoAgrupado(alojamiento, fechaCotaInferior, fechaCotaSuperior, token, agrupamiento, ordenamiento)).build();
			}
			else {
				return Response.status(200).entity(tm.darClientesConReservaEnRango(alojamiento, fechaCotaInferior, fechaCotaSuperior, token, ordenamiento)).build();
			}
			
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("/clientessinreservasrango")
	public Response darClientesSinReservaEnRango(@QueryParam("token") Long token, @QueryParam("alojamiento") Long alojamiento, 
			@QueryParam("fechaCotaInferior") Date fechaCotaInferior, @QueryParam("fechaCotaSuperior") Date fechaCotaSuperior,
			@QueryParam("agrupamiento") Integer agrupamiento, @QueryParam("ordenamiento") Integer ordenamiento) {
		
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			
			if(agrupamiento != null) {
				return Response.status(200).entity(tm.darClientesSinReservaEnRangoAgrupado(alojamiento, fechaCotaInferior, fechaCotaSuperior, token, agrupamiento, ordenamiento)).build();
			}
			
			else {
				return Response.status(200).entity(tm.darClientesSinReservaEnRango(alojamiento, fechaCotaInferior, fechaCotaSuperior, token, ordenamiento)).build();
			}
		}
		catch(BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		}
		catch(Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	
	@SuppressWarnings("deprecation")
	private void corregirDesfase(Cliente cliente) {
		
		cliente.getFechaCreacion().setDate(cliente.getFechaCreacion().getDate()+1);
		
	}
	
}
