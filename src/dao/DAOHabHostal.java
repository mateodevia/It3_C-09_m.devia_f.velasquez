package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.HabHost;
import vos.HabHostal;
import vos.Hostal;
import vos.PersonaNatural;

public class DAOHabHostal {
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
	
	public DAOHabHostal() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos las habHostal en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las abHost que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabHostal> getHabHostal() throws SQLException, Exception {
		
		ArrayList<HabHostal> habHostales = new ArrayList<HabHostal>();

		String sql = String.format("SELECT * FROM %1$s.HABS_HOSTALES", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			habHostales.add(convertResultSetToHabHostal(rs));
		}
		
		st.close();
		return habHostales;
	}
	
	/**
	 * Metodo que obtiene la informacion de la habHostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la habHost
	 * @return la informacion de la habHostal que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la habHostal con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public HabHostal findHabHostalById(Long id) throws SQLException, Exception 
	{
		HabHostal habHostal = null;

		String sql = String.format("SELECT * FROM %1$s.HABS_HOSTALES WHERE ID_AL = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			habHostal = convertResultSetToHabHostal(rs);
		}

		st.close();
		return habHostal;
	}
	/**
	 * Metodo que agregar la informacion de una nueva habHostal en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHostal HabHostal que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHabHostal(HabHostal habHostal) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HABS_HOSTALES (ID_AL, NUMERO, NUM_COMP) VALUES (%2$s, %3$s, %4$s)", 
									USUARIO, 
									habHostal.getId(),
									habHostal.getNumero(),
									habHostal.getNumComparte());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion de la habHostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHostal HabHostal que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHabHostal(HabHostal habHostal) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.HABS_HOSTALES SET ", USUARIO));
		sql.append(String.format("ID_AL	 = %1$s AND NUMERO = %2$s AND NUM_COMP = %3$s",
					habHostal.getId(),
					habHostal.getNumero(),
					habHostal.getNumComparte()));
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la habHostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habHostal HabHostal que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHabHostal(HabHostal habHostal) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HABS_HOSTALES WHERE ID_AL = %2$d", USUARIO, habHostal.getId());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos las habsHostal de un hostal <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las habitaciones de un hostal
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabHostal> getHabsHostalOfHostal( long id ) throws SQLException, Exception {
		
		ArrayList<HabHostal> habHostal = new ArrayList<HabHostal>();

		String sql = String.format("SELECT * FROM %1$s.HABS_HOSTALES INNER JOIN %1$s.ALOJAMIENTOS ON ID = ID_AL WHERE ID_OP = %2$s", USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			habHostal.add(convertResultSetToHabHostal(rs));
		}
		
		st.close();
		return habHostal;
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
	public HabHostal convertResultSetToHabHostal(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_AL");
		int numero = resultSet.getInt("NUMERO");
		int numComparte = resultSet.getInt("NUM_COMP");
		long idOp = resultSet.getLong("ID_OP");
		
		DAOAlojamiento daoAloja = new DAOAlojamiento();
		daoAloja.setConn(conn);
		Alojamiento aloja = daoAloja.findAlojamientoById(id);
				
		HabHostal habHostal = new HabHostal(id, aloja.getCapacidad(), aloja.getCompartida(), aloja.getTipoAlojamiento(), aloja.getUbicacion(), aloja.getServicios(), aloja.getOfertas(), numero, numComparte, idOp);

		return habHostal;
	}
}
