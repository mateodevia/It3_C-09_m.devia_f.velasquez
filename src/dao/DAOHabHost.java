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
import vos.Operador;
import vos.PersonaNatural;
import vos.Servicio;

public class DAOHabHost {
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
	
	public DAOHabHost() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos las habHost en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las abHost que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabHost> getHabHost() throws SQLException, Exception {
		
		ArrayList<HabHost> habHosts = new ArrayList<HabHost>();

		String sql = String.format("SELECT * FROM %1$s.HABS_HOST", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			habHosts.add(convertResultSetToHabHost(rs));
		}
		
		st.close();
		return habHosts;
	}
	
	/**
	 * Metodo que obtiene la informacion de la habHost en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la habHost
	 * @return la informacion de la habHost que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la habHost con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public HabHost findHabHostById(Long id) throws SQLException, Exception 
	{
		HabHost habHost = null;

		String sql = String.format("SELECT * FROM %1$s.HABS_HOSTS WHERE ID_AL = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			habHost = convertResultSetToHabHost(rs);
		}

		
		st.close();
		return habHost;
	}
	/**
	 * Metodo que agregar la informacion de una nueva habHost en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHost HabHost que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHabHost(HabHost habHost) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HABS_HOSTS (ID_AL, NUM_COMP) VALUES (%2$s, %3$s)", 
									USUARIO, 
									habHost.getId(),
									habHost.getNumComparte());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
		st.close();
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();

	}
	
	/**
	 * Metodo que actualiza la informacion de la habHost en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHost HabHost que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHabHost(HabHost habHost) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.HABS_HOSTS SET ", USUARIO));
		sql.append(String.format("ID_AL	 = %1$s AND NUM_COMP = %2$s",
					habHost.getId(),
					habHost.getNumComparte()));

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la habHost en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHost HabHost que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHabHost(HabHost habHost) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HABS_HOSTS WHERE ID_AL = %2$d", USUARIO, habHost.getId());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos las habsHost de una personas <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las habitaciones de una persona
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabHost> getHabsHostOfPersonaNatural( long id ) throws SQLException, Exception {
		
		ArrayList<HabHost> habHost = new ArrayList<HabHost>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS INNER JOIN %1$s.HABS_HOSTS ON ALOJAMIENTOS.ID = HABS_HOSTS.ID_AL WHERE ID_OP = %2$s.",
				USUARIO, id);
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			habHost.add(convertResultSetToHabHost(rs));
		}
		
		st.close();
		return habHost;
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
	public HabHost convertResultSetToHabHost(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_AL");
		int numComparte = resultSet.getInt("NUM_COMP");
		
		
		DAOAlojamiento daoAloja = new DAOAlojamiento();
		daoAloja.setConn(conn);
		Alojamiento aloja = daoAloja.findAlojamientoById(id);
				
		HabHost habHost = new HabHost(id, aloja.getCapacidad(), aloja.getCompartida(), aloja.getTipoAlojamiento(), aloja.getUbicacion(), aloja.getServicios(), aloja.getOfertas(), numComparte,aloja.getOperador());

		return habHost;
	}
}
