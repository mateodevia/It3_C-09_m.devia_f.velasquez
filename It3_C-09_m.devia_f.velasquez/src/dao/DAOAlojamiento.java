package dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import auxiliary.Fechas;
import vos.Alojamiento;
import vos.OfertaAlojamiento;
import vos.Servicio;

public class DAOAlojamiento {

	//----------------------------------------------------------------------------------------------
	// Constantes
	//----------------------------------------------------------------------------------------------
	
	public final static String FELIPE = "ISIS2304A1001810";
	public final static String MATEO = "ISIS2304A821810";
	
	public final static String USUARIO = FELIPE;
	
	//----------------------------------------------------------------------------------------------
	// Atributos
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;
	
	//----------------------------------------------------------------------------------------------
	// Constructor
	//----------------------------------------------------------------------------------------------
	
	public DAOAlojamiento() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Alojamiento> getAlojamientos() throws SQLException, Exception {
		ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			alojamientos.add(convertResultSetToAlojamiento(rs));
		}
		
		st.close();
		return alojamientos;
	}
	
	/**
	 * Metodo que obtiene la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del alojamiento
	 * @return la informacion del alojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el alojamiento con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Alojamiento findAlojamientoById(Long id) throws SQLException, Exception 
	{
		Alojamiento alojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS WHERE ID = %2$d", USUARIO, id);
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			alojamiento = convertResultSetToAlojamiento(rs);
		}

		st.close();
		return alojamiento;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo alojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		int bool = 0;
		
		if (alojamiento.getCompartida()) {
			
			bool = 1;
		}
		
		String sql = String.format("INSERT INTO %1$s.ALOJAMIENTOS (ID, CAPACIDAD, COMPARTIDA, TIPO, UBICACION, ID_OP) VALUES (%2$s, %3$s, %4$s, '%5$s', '%6$s', %7$s)", 
									USUARIO, 
									alojamiento.getId(),
									alojamiento.getCapacidad(),
									bool,
									alojamiento.getTipoAlojamiento(),
									alojamiento.getUbicacion(),
									alojamiento.getOperador());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.ALOJAMIENTOS SET ", USUARIO));
		sql.append(String.format("ID = '%1$s' AND CAPACIDAD = '%2$s' AND COMPARTIDA = '%3$s' AND TIPO = '%4$s'AND UBICACION = '%5$s'",
					alojamiento.getId(), 
					alojamiento.getCapacidad(), 
					alojamiento.getCompartida(), 
					alojamiento.getTipoAlojamiento(), 
					alojamiento.getUbicacion()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.ALOJAMIENTOS WHERE ID = %2$d", USUARIO, alojamiento.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	public List<Alojamiento> getAlojamientosBajaDemanda() throws SQLException, Exception {

		String sql = String.format("SELECT %1$s.ALOJAMIENTOS.*, DIAS_MAX_SIN_RESV FROM %1$s.ALOJAMIENTOS LEFT JOIN (SELECT ID_AL_OF, MAX( ROUND(CASE WHEN PROX_INICIO IS NOT NULL THEN FECHA_INICIO ELSE CURRENT_DATE END - FECHA_FIN,0)) AS DIAS_MAX_SIN_RESV FROM ( SELECT %1$s.RESERVAS.*, LEAD(FECHA_INICIO,1) OVER (PARTITION BY ID_AL_OF ORDER BY FECHA_INICIO) AS PROX_INICIO FROM %1$s.RESERVAS WHERE %1$s.RESERVAS.ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE ) GROUP BY ID_AL_OF) ON %1$s.ALOJAMIENTOS.ID = ID_AL_OF WHERE DIAS_MAX_SIN_RESV > 30 OR DIAS_MAX_SIN_RESV IS NULL",
				USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		List<Alojamiento> resp = new ArrayList<Alojamiento>();
		while(rs.next()) {
			resp.add(convertResultSetToAlojamiento(rs));
		}
		
		st.close();
		return resp;
	}
	
	/**
	 * Metodo que obtiene la informacion de todos los alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Servicio> getServiciosOfAlojamiento( long id ) throws SQLException, Exception {
		ArrayList<Servicio> servicios = new ArrayList<Servicio>();

		String sql = String.format("SELECT * FROM %1$s.ALOJ_SERV WHERE ID_ALOJ= %2$S", USUARIO, id);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			DAOServicio dao = new DAOServicio();
			dao.setConn(conn);
			Servicio servicio = dao.findServicioById(rs.getLong("ID_SERV"));
			
			servicios.add(servicio);
		}
		
		st.close();
		return servicios;
	}
	
	public long generateNewId() throws SQLException {
		
		String sql = String.format("SELECT MAX(ID) FROM %1$s.ALOJAMIENTOS", USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		rs.next();
		
		long idMax = rs.getLong("MAX(ID)");
		
		
		st.close();
		return idMax+1;
	}

	public List<Alojamiento> getAlojamientosEntreFechasConServicios( Date fechaInicio, Date fechFin, List<Servicio> servicios ) throws Exception {

		ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();

		String sql = "";
		
		if(!servicios.isEmpty()) {

			sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS WHERE EXISTS (SELECT * FROM (SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s' OR FECHA_INICIO = '%2$s'OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL))DISPONIBLES INNER JOIN %1$s.ALOJ_SERV ON DISPONIBLES.ID_AL = %1$s.ALOJ_SERV.ID_ALOJ WHERE (ID_AL= %1$s.ALOJAMIENTOS.ID AND ID_SERV = %4$s))",
					USUARIO, 
					Fechas.pasarDateAFormatoSQL(fechaInicio), 
					Fechas.pasarDateAFormatoSQL(fechFin),
					servicios.get(0).getId());

			System.out.println("SERVICIOS.size " + servicios.size());
			if (servicios.size() > 1) {

				for(int i = 1; i < servicios.size(); i++) {

					String adicion = String.format("AND EXISTS (SELECT * FROM (SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s' OR FECHA_INICIO = '%2$s'OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL))DISPONIBLES INNER JOIN %1$s.ALOJ_SERV ON DISPONIBLES.ID_AL = %1$s.ALOJ_SERV.ID_ALOJ WHERE (ID_AL= %1$s.ALOJAMIENTOS.ID AND ID_SERV = %4$s))",
							USUARIO, 
							Fechas.pasarDateAFormatoSQL(fechaInicio), 
							Fechas.pasarDateAFormatoSQL(fechFin),
							servicios.get(i).getId());

					sql = sql + adicion;
				}
			}
		}
		else {
			
			sql = String.format("SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s'OR FECHA_INICIO = '%2$s' OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL)",
					USUARIO, 
					Fechas.pasarDateAFormatoSQL(fechaInicio), 
					Fechas.pasarDateAFormatoSQL(fechFin));
		}

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );

		while (rs.next()) {
			
			alojamientos.add(convertResultSetToAlojamiento(rs));
		}
		
		return alojamientos;
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos Auxiliares
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * @param connection la conexion generada en el TransactionManager para la comunicacion con la Base de Datos
	 */
	public void setConn(Connection connection){
		this.conn = connection;
	}
	
	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido cerrados.
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}
		
	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL en una instancia java.
	 * @param resultSet ResultSet que se obtuvo de la base de datos.
	 * @return Alojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla.
	 * @throws Exception 
	 */
	public Alojamiento convertResultSetToAlojamiento(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID");
		int capacidad = resultSet.getInt("CAPACIDAD");
		boolean compartida = resultSet.getBoolean("COMPARTIDA");
		String tipo = resultSet.getString("TIPO");
		String ubicacion = resultSet.getString("UBICACION");
		long idOp = resultSet.getLong("ID_OP");
		ArrayList<Servicio> servicios = getServiciosOfAlojamiento(id);
		
		DAOOfertaAlojamiento daoOferta = new DAOOfertaAlojamiento();
		daoOferta.setConn(conn);
		List<OfertaAlojamiento> ofertas = daoOferta.getOfertasOfAlojamiento(id);

		Alojamiento alojamiento = new Alojamiento(id, capacidad, compartida, tipo, ubicacion, servicios, ofertas, idOp);

		return alojamiento;
	}
	
	
	
}
