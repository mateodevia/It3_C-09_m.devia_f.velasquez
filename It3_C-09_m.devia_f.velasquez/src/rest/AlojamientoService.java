package rest;

import java.sql.Date;
import java.util.List;

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

import auxiliary.RFC3;
import dao.DAOServicio;
import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.Alojamiento;
import vos.Apartamento;
import vos.FechasServicios;
import vos.Reserva;
import vos.Servicio;

@Path("alojamientos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlojamientoService {

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

	@GET
	public Response getIndiceOcupacion() {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

		try {
			List<RFC3> indices = tm.getIndiceOcupacion();
			return Response.status(200).entity(indices).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Path("rfc4")
	public Response getAlojamientosFechasServicios(@QueryParam(value = "fechaInicio") Date fechaInicio,
			@QueryParam(value = "fechaFin") Date fechaFin, @QueryParam(value = "servicios") List<String> servicios) {

		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

		System.out.println("FI " + fechaInicio);

		/*
		 * obj.getFechaInicio().setDate(obj.getFechaInicio().getDate()+1);
		 * obj.getFechaFin().setDate(obj.getFechaInicio().getDate()+1);
		 */
		try {

			List<Alojamiento> alojamientos = tm.getAlojamientosEntreFechasConServicios(fechaInicio, fechaFin,
					servicios);
			return Response.status(200).entity(alojamientos).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	@Path("/bajademanda")
	public Response getAlojamientosBajaDemanda() {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		try {
			return Response.status(200).entity(tm.getAlojamientoBajaDemanda()).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

}
