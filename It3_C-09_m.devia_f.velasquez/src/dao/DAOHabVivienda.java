package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.HabHost;
import vos.HabVivienda;
import vos.PersonaNatural;
import vos.ViviendaUniversitaria;

public class DAOHabVivienda {
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
	
	public DAOHabVivienda() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos las habVivienda en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las habVivienda que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabVivienda> getHabVivienda() throws SQLException, Exception {
		
		ArrayList<HabVivienda> habViviendas = new ArrayList<HabVivienda>();

		String sql = String.format("SELECT * FROM %1$s.HABS_VIVIENDAS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			habViviendas.add(convertResultSetToHabVivienda(rs));
		}
		
		st.close();
		return habViviendas;
	}
	
	/**
	 * Metodo que obtiene la informacion de la habVivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la habVivienda
	 * @return la informacion de la habVivienda que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la habVivienda con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public HabVivienda findHabViviendaById(Long id) throws SQLException, Exception 
	{
		HabVivienda habVivienda = null;

		String sql = String.format("SELECT * FROM %1$s.HABS_VIVIENDAS WHERE ID_AL = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			habVivienda = convertResultSetToHabVivienda(rs);
		}

		st.close();
		return habVivienda;
	}
	/**
	 * Metodo que agregar la informacion de una nueva habVivienda en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habVivienda HabVivienda que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHabVivienda(HabVivienda habVivienda) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HABS_VIVIENDAS (ID_AL, NUM_HAB, NUM_COMP) VALUES (%2$s, '%3$s', '%4$s')", 
									USUARIO, 
									habVivienda.getId(),
									habVivienda.getNumeroHabitacion(),
									habVivienda.getNumeroComparte());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion de la habVivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habVivienda HabVivienda que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHabVivienda(HabVivienda habVivienda) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.HABS_VIVIENDAS SET ", USUARIO));
		sql.append(String.format("ID_AL	 = '%1$s' AND NUM_HAB = '%2$s' AND NUM_COMP = '%3$s'",
					habVivienda.getId(),
					habVivienda.getNumeroHabitacion(),
					habVivienda.getNumeroComparte()));
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la habVivienda en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habVivienda HabVivienda que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHabVivienda(HabVivienda habVivienda) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HABS_VIVIENDAS WHERE ID_AL = %2$d", USUARIO, habVivienda.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos las habVivienda de una vivienda <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las habitaciones de una vivienda
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<HabVivienda> getHabViviendaOfViviendaUniversitaria( long id ) throws SQLException, Exception {
		
		ArrayList<HabVivienda> habVivienda = new ArrayList<HabVivienda>();

		String sql = String.format("SELECT * FROM %1$s.HABS_VIVIENDAS INNER JOIN %1$s.ALOJAMIENTOS ON ID = ID_AL WHERE ID_OP = %2$s", USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			habVivienda.add(convertResultSetToHabVivienda(rs));
		}
		
		st.close();
		return habVivienda;
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
	public HabVivienda convertResultSetToHabVivienda(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_AL");
		int numHab = resultSet.getInt("NUM_HAB");
		int numComparte = resultSet.getInt("NUM_COMP");
		long idOp = resultSet.getLong("ID_OP");
		
		DAOAlojamiento daoAloja = new DAOAlojamiento();
		daoAloja.setConn(conn);
		Alojamiento aloja = daoAloja.findAlojamientoById(id);
		
		HabVivienda habVivienda = new HabVivienda(id, aloja.getCapacidad(), aloja.getCompartida(), aloja.getTipoAlojamiento(), aloja.getUbicacion(), aloja.getServicios(), aloja.getOfertas(), numHab, numComparte, idOp);

		return habVivienda;
	}
}
