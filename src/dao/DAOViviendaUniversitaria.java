package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.HabHostal;
import vos.HabVivienda;
import vos.Hostal;
import vos.Operador;
import vos.ViviendaUniversitaria;

public class DAOViviendaUniversitaria {
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
	
	public DAOViviendaUniversitaria() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todas las viviendaUniversitaria en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las viviendaUniversitaria que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<ViviendaUniversitaria> getViviendaUniversitaria() throws SQLException, Exception {
		ArrayList<ViviendaUniversitaria> viviendaUniversitaria = new ArrayList<ViviendaUniversitaria>();

		String sql = String.format("SELECT * FROM %1$s.HOSTALES", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			viviendaUniversitaria.add(convertResultSetToViviendaUniversitaria(rs));
		}
		
		st.close();
		return viviendaUniversitaria;
	}
	
	/**
	 * Metodo que obtiene la informacion de la viviendaUniversitaria en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la viviendaUniversitaria
	 * @return la informacion de la viviendaUniversitaria que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe de la viviendaUniversitaria con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ViviendaUniversitaria findViviendaUniversitariaById(Long id) throws SQLException, Exception 
	{
		ViviendaUniversitaria viviendaUniversitaria = null;

		String sql = String.format("SELECT * FROM %1$s.VIVIENDAS_UNI WHERE ID_OP = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			viviendaUniversitaria = convertResultSetToViviendaUniversitaria(rs);
		}

		st.close();
		return viviendaUniversitaria;
	}
	/**
	 * Metodo que agregar la informacion de una nueva viviendaUniversitaria en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param viviendaUniversitaria ViviendaUniversitaria que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.VIVIENDAS_UNI (ID_OP, DIRECCION) VALUES (%2$s, '%3$s')", 
									USUARIO, 
									viviendaUniversitaria.getId(),
									viviendaUniversitaria.getDireccion());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del viviendaUniversitaria en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param viviendaUniversitaria ViviendaUniversitaria que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.VIVIENDAS_UNI SET ", USUARIO));
		sql.append(String.format("ID_OP	 = '%1$s' AND DIRECCION = '%2$s'",
					viviendaUniversitaria.getId(),
					viviendaUniversitaria.getDireccion()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la viviendaUniversitaria en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param viviendaUniversitaria ViviendaUniversitaria que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteViviendaUniversitaria(ViviendaUniversitaria viviendaUniversitaria) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.VIVIENDAS_UNI WHERE ID_OP = %2$d", USUARIO, viviendaUniversitaria.getId());

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
	public ViviendaUniversitaria convertResultSetToViviendaUniversitaria(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_OP");
		
		DAOOperador daoOp = new DAOOperador();
		daoOp.setConn(conn);
		Operador op = daoOp.findOperadorById(id);
		
		String direccion = resultSet.getString("DIRECCION");
		
		DAOHabVivienda daoHab = new DAOHabVivienda();
		daoHab.setConn(conn);
		ArrayList<HabVivienda> habitaciones = daoHab.getHabViviendaOfViviendaUniversitaria(id);
		
		ViviendaUniversitaria viviendaUniversitaria = new ViviendaUniversitaria(id, op.getNombre(), op.getTipo(), direccion, habitaciones);
		
		return viviendaUniversitaria;
	}
}
