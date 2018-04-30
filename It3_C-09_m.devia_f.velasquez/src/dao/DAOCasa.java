package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.Casa;
import vos.HabHost;
import vos.OfertaAlojamiento;
import vos.Operador;
import vos.PersonaNatural;
import vos.Servicio;

public class DAOCasa {
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
	
	public DAOCasa() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos las casas en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos las casas que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Casa> getCasas() throws SQLException, Exception {
		ArrayList<Casa> casas = new ArrayList<Casa>();

		String sql = String.format("SELECT * FROM %1$s.CASAS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			casas.add(convertResultSetToCasa(rs));
		}
		
		st.close();
		return casas;
	}
	
	/**
	 * Metodo que obtiene la informacion de la casa en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la casa
	 * @return la informacion de la casa que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la casa con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Casa findCasaById(Long id) throws SQLException, Exception 
	{
		Casa casa = null;

		String sql = String.format("SELECT * FROM %1$s.CASAS WHERE ID_AL = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			casa = convertResultSetToCasa(rs);
		}

		st.close();
		return casa;
	}
	/**
	 * Metodo que agregar la informacion de una nueva casa en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param casa Casa que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addCasa(Casa casa) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.CASAS (ID_AL, MENAJE, HABITACIONES, SEGURO) VALUES (%2$s, '%3$s', '%4$s', '%5$s')", 
									USUARIO, 
									casa.getId(),
									casa.getMenaje(),
									casa.getHabitaciones(),
									casa.getSeguro());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion de la casa en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param casa Casa que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateCasa(Casa casa) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.CASAS SET ", USUARIO));
		sql.append(String.format("ID_AL	 = '%1$s' AND MENAJE = '%2$s' AND HABITACIONES = '%3$s' AND SEGURO = '%4$s'",
					casa.getId(),
					casa.getMenaje(),
					casa.getHabitaciones(),
					casa.getSeguro()));
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la casa en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param casa Casa que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteCasa(Casa casa) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CASAS WHERE ID_AL = %2$d", USUARIO, casa.getId());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos las casas de una persona <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las casas de una persona
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Casa> getCasasOfPersonaNatural( long id ) throws SQLException, Exception {
		
		ArrayList<Casa> casas = new ArrayList<Casa>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS INNER JOIN %1$s.CASAS ON ID_AL = ID WHERE ID_OP = %2$d", USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			casas.add(convertResultSetToCasa(rs));
		}
		
		st.close();
		return casas;
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
	public Casa convertResultSetToCasa(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_AL");
		
		DAOAlojamiento daoAloja = new DAOAlojamiento();
		daoAloja.setConn(conn);
		Alojamiento aloja = daoAloja.findAlojamientoById(id);
		
		String menaje = resultSet.getString("MENAJE");
		int habitaciones = resultSet.getInt("HABITACIONES");
		String seguro = resultSet.getString("SEGURO");
		long idOp = resultSet.getLong("ID_OP");
		
		Casa casa = new Casa(id, aloja.getCapacidad(), aloja.getCompartida(), aloja.getTipoAlojamiento(), aloja.getUbicacion(), aloja.getServicios(), aloja.getOfertas(), menaje, habitaciones, seguro, idOp);

		return casa;
	}
}
