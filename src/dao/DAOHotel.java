package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.HabHostal;
import vos.HabHotel;
import vos.Hostal;
import vos.Hotel;
import vos.Operador;

public class DAOHotel {
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
	
	public DAOHotel() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los hoteles en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los hoteles que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Hotel> getHoteles() throws SQLException, Exception {
		ArrayList<Hotel> hoteles = new ArrayList<Hotel>();

		String sql = String.format("SELECT * FROM %1$s.HOTELES", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		while (rs.next()) {
			hoteles.add(convertResultSetToHotel(rs));
		}
		
		st.close();
		return hoteles;
	}
	
	/**
	 * Metodo que obtiene la informacion de la hotel en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del hostal
	 * @return la informacion del hotel que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe del hotel con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Hotel findHotelById(Long id) throws SQLException, Exception 
	{
		Hotel hotel = null;

		String sql = String.format("SELECT * FROM %1$s.HOTELES WHERE ID_OP = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			hotel = convertResultSetToHotel(rs);
		}

		st.close();
		return hotel;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo hotel en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hotel Hotel que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHotel(Hotel hotel) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HOTELES (ID_OP, DIRECCION, REGISTRO_CAMARA, REGISTRO_SUPER) VALUES (%2$s, '%3$s', '%4$s', '%5$s')", 
									USUARIO, 
									hotel.getId(),
									hotel.getDireccion(),
									hotel.getRegistroCamara(),
									hotel.getRegistroSuper());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del hotel en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hotel Hotel que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHotel(Hotel hotel) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.HOTELES SET ", USUARIO));
		sql.append(String.format("ID_OP	 = '%1$s' AND DIRECCION = '%2$s'AND REGISTRO_CAMARA = '%3$s' AND REGISTRO_SUPER = '%3$s'",
					hotel.getId(),
					hotel.getDireccion(),
					hotel.getRegistroCamara(),
					hotel.getRegistroSuper()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del hotel en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hotel Hotel que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHotel(Hotel hotel) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HOTELES WHERE ID_OP = %2$d", USUARIO, hotel.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
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
	public Hotel convertResultSetToHotel(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_OP");
		
		DAOOperador daoOP = new DAOOperador();
		daoOP.setConn(conn);
		Operador op = daoOP.findOperadorById(id);
		
		String direccion = resultSet.getString("DIRECCION");
		String registroCamara = resultSet.getString("REGISTRO_CAMARA");
		String registroSuper = resultSet.getString("REGISTRO_SUPER");
		
		DAOHabHotel daoHabHotet = new DAOHabHotel();
		daoHabHotet.setConn(conn);
		ArrayList<HabHotel> habitaciones = daoHabHotet.getHabHotelOfHotel(id);
		
		Hotel hotel = new Hotel(id, op.getNombre(), op.getTipo(), direccion, registroCamara, registroSuper, habitaciones);
		
		return hotel;
	}

}
