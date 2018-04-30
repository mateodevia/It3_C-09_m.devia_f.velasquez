package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Apartamento;
import vos.Casa;
import vos.HabHost;
import vos.HabHostal;
import vos.Hostal;
import vos.Operador;
import vos.PersonaNatural;

public class DAOHostal {
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
	
	public DAOHostal() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los hostales en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los hostales que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Hostal> getHostales() throws SQLException, Exception {
		ArrayList<Hostal> hostales = new ArrayList<Hostal>();

		String sql = String.format("SELECT * FROM %1$s.HOSTALES", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			hostales.add(convertResultSetToHostal(rs));
		}
		
		st.close();
		return hostales;
	}
	
	/**
	 * Metodo que obtiene la informacion de la hostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del hostal
	 * @return la informacion del hostal que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe del hostal con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Hostal findHostalById(Long id) throws SQLException, Exception 
	{
		Hostal hostal = null;

		String sql = String.format("SELECT * FROM %1$s.HOSTALES WHERE ID_OP = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			hostal = convertResultSetToHostal(rs);
		}

		st.close();
		return hostal;
	}
	/**
	 * Metodo que agregar la informacion de una nueva hostal en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hostal Hostal que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHostal(Hostal hostal) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HOSTALES (ID_OP, HORARIO_APERTURA, HORARIO_CIERRE, DIRECCION, REGISTRO_CAMARA, REGISTRO_SUPER) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", 
									USUARIO, 
									hostal.getId(),
									hostal.getHorarioApertura(),
									hostal.getHorarioCierre(),
									hostal.getDireccion(),
									hostal.getRegistroCamara(),
									hostal.getRegistroSuper());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del hostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hostal Hostal que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHostal(Hostal hostal) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.HOSTALES SET ", USUARIO));
		sql.append(String.format("ID_OP	 = '%1$s' AND HORARIO_APERTURA = '%2$s' AND HORARIO_CIERRE = '%3$s' AND DIRECCION = '%4$s'AND REGISTRO_CAMARA = '%5$s' AND REGISTRO_SUPER = '%6$s'",
					hostal.getId(),
					hostal.getHorarioApertura(),
					hostal.getHorarioCierre(),
					hostal.getDireccion(),
					hostal.getRegistroCamara(),
					hostal.getRegistroSuper()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del hostal en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param hostal Hostal que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHostal(Hostal hostal) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HOSTALES WHERE ID_OP = %2$d", USUARIO, hostal.getId());

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
	public Hostal convertResultSetToHostal(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_OP");
		
		DAOOperador daoOperador = new DAOOperador();
		daoOperador.setConn(conn);
		Operador op = daoOperador.findOperadorById(id);
		
		String horarioApertura = resultSet.getString("HORARIO_APERTURA");
		String horarioCierre = resultSet.getString("HORARIO_CIERRE");
		String direccion = resultSet.getString("DIRECCION");
		String registroCamara = resultSet.getString("REGISTRO_CAMARA");
		String registroSuper = resultSet.getString("REGISTRO_SUPER");
		
		DAOHabHostal daoHab = new DAOHabHostal();
		daoHab.setConn(conn);
		ArrayList<HabHostal> habitaciones = daoHab.getHabsHostalOfHostal(id);
		
		Hostal hostal = new Hostal(id, op.getNombre(), op.getTipo(), horarioApertura, horarioCierre, direccion, registroCamara, registroSuper, habitaciones);
		
		return hostal;
	}
}
