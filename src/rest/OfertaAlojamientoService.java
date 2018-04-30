package rest;

import java.sql.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import auxiliary.Log;
import exceptions.BusinessLogicException;
import tm.AlohAndesTransactionManager;
import vos.OfertaAlojamiento;

@Path("ofertasalojamiento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OfertaAlojamientoService {

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
		System.out.println("ATENCION " + context.getRealPath("/"));
		System.out.println("ATENCIONx2 " + context.getRealPath("/webapp"));
		System.out.println("ATENCION " + context.getRealPath("/WEB-INF/ConnectionData"));
		return context.getRealPath("/WEB-INF/ConnectionData");
	}

	private String doErrorMessage(Exception e) {
		return "{ \"ERROR\": \"" + e.getMessage() + "\"}";
	}

	// ----------------------------
	// SERVICIOS
	// ----------------------------

	@POST
	public Response postOfertaAlojamiento(OfertaAlojamiento oa, @QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

		correctorDesfase(oa);
		
		try {
			tm.registrarOfertaAlojamiento(oa, token);
			return Response.status(200).entity(oa).build();
		} catch (BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@PUT
	public Response putOfertaAlojamiento(OfertaAlojamiento oa, @QueryParam("token") Long token) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		correctorDesfase(oa);
		
		try {
			tm.retirarOferta(oa, token);
			return Response.status(200).entity(oa).build();
		} catch (BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	@GET
	public Response getNOfertasMasPopulares(@QueryParam("num") Integer num) {
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		
		try {
			List<OfertaAlojamiento> li= tm.getOfertasMasPopulares(num);
			return Response.status(200).entity(li).build();
		} catch (BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@PUT
	@Path("/deshabilitar")
	public Response deshabilitarOferta(@QueryParam(value = "idAl") Long idAl, 
			@QueryParam(value = "fechaCreacion") Date fechaCreacion,
			@QueryParam(value = "fechaDeshabilitamiento") Date fechaDeshabilitamiento, 
			@QueryParam(value = "token") Long token) {
		
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			String s = tm.deshabilitarReservaAlojamiento(idAl, fechaCreacion, fechaDeshabilitamiento, token);
			System.out.println(s);
			return Response.status(200).entity(new Log(s)).build();
		} catch (BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@PUT
	@Path("/rehabilitar")
	public Response rehabilitarOferta(@QueryParam(value = "idAl") Long idAl, @QueryParam(value = "fechaCreacion") Date fechaCreacion, 
			@QueryParam(value = "fechaRehabilitacion") Date fechaRehabilitacion, @QueryParam(value = "token") Long token) {
		
		AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());
		
		try {
			return Response.status(200).entity(tm.rehabilitarOferta(idAl, fechaCreacion, fechaRehabilitacion, token)).build();
		} catch (BusinessLogicException e) {
			return Response.status(400).entity(doErrorMessage(e)).build();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void correctorDesfase(OfertaAlojamiento oa) {
		//Para corregir desfase
				if(oa.getFechaRetiro()!=null)
					oa.getFechaRetiro().setDate(oa.getFechaRetiro().getDate()+1);
				if(oa.getFechaCreacion()!=null)
					oa.getFechaCreacion().setDate(oa.getFechaCreacion().getDate()+1);
	}
}
