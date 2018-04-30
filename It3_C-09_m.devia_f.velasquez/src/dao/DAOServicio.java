package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import vos.Servicio;

public class DAOServicio {
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
	
	public DAOServicio() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los servicio en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los servicios que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Servicio> getServicio() throws SQLException, Exception {
		ArrayList<Servicio> servicio = new ArrayList<Servicio>();

		String sql = String.format("SELECT * FROM %1$s.SERVICIOS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			servicio.add(convertResultSetToServicio(rs));
		}
		
		st.close();
		return servicio;
	}
	
	/**
	 * Metodo que obtiene la informacion del servicios en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del servicio
	 * @return la informacion del servicio que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el servicio con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Servicio findServicioById(Long id) throws SQLException, Exception 
	{
		Servicio servicio = null;

		String sql = String.format("SELECT * FROM %1$s.SERVICIOS WHERE ID = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			servicio = convertResultSetToServicio(rs);
		}

		st.close();
		return servicio;
	}
	
	public Servicio findServicioByTipo(String tipo) throws SQLException, Exception 
	{
		Servicio servicio = null;

		String sql = String.format("SELECT * FROM %1$s.SERVICIOS WHERE TIPO = '%2$s'", USUARIO, tipo); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			servicio = convertResultSetToServicio(rs);
		}

		st.close();
		return servicio;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo servicio en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicio Servicio que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addServicio(Servicio servicio) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.SERVICIOS (ID, TIPO, COSTO) VALUES (%2$s, '%3$s', '%4$s')", 
									USUARIO, 
									servicio.getId(),
									servicio.getTipo(),
									servicio.getCosto());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del servicio en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicio Servicio que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateServicio(Servicio servicio) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.SERVICIOS SET ", USUARIO));
		sql.append(String.format("ID = %1$s AND TIPO = %2$s AND COSTO = %3$s",
					servicio.getId(),
					servicio.getTipo(),
					servicio.getCosto()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del servicio en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicio Servicio que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteServicio(Servicio servicio) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.SERVICIOS WHERE ID = %2$d", USUARIO, servicio.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que devuelve todas las reservas de una reserva colectiva
	 * @param id de la reserva colectiva
	 * @return la lista de las reservas
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Servicio> getServiciosOfReservaColectiva( long idColectiva ) throws SQLException, Exception {
		ArrayList<Servicio> servicios = new ArrayList<Servicio>();

		String sql = String.format("SELECT * FROM %1$s.RESERV_COL_SERV INNER JOIN SERVICIOS ON ID = ID_SERV WHERE ID_RESERV_COL = %2$s", USUARIO, idColectiva);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			servicios.add(convertResultSetToServicio(rs));
			
		}
		
		st.close();
		return servicios;
	}
	
	public long generateNewId() throws SQLException {
		
		String sql = String.format("SELECT MAX(ID) FROM %1$s.SERVICIOS", USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		rs.next();
		
		long idMax = rs.getLong("MAX(ID)");
		
		st.close();
		return idMax+1;
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
	public Servicio convertResultSetToServicio(ResultSet resultSet) throws Exception {
		
		Long id = resultSet.getLong("ID");
		String tipo = resultSet.getString("TIPO");
		double costo = resultSet.getDouble("COSTO");
		
		DAOReservaColectiva daoReservaColectiva = new DAOReservaColectiva();
		
		daoReservaColectiva.setConn(conn);
		
		ArrayList<Long> reservasColectivas = daoReservaColectiva.getReservasColectivasWithServicio(id);

		Servicio servicio = new Servicio(id, tipo, costo, reservasColectivas);

		return servicio;
	}
}
