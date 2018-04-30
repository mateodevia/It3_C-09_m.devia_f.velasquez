package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.Apartamento;
import vos.Casa;
import vos.HabHost;
import vos.Operador;
import vos.PersonaNatural;

public class DAOPersonaNatural {
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
	
	public DAOPersonaNatural() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos las personaNaturales en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos las personaNaturals que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<PersonaNatural> getPersonasNaturales() throws SQLException, Exception {
		
		ArrayList<PersonaNatural> personaNaturales = new ArrayList<PersonaNatural>();

		String sql = String.format("SELECT * FROM %1$s.PERSONAS_NAT", USUARIO);
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		
//		recursos.add(prepStmt);
//		
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			personaNaturales.add(convertResultSetToPersonaNatural(rs));
		}
		
		
		st.close();
		return personaNaturales;
	}
	
	/**
	 * Metodo que obtiene la informacion de la personaNatural en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la casa
	 * @return la informacion de la personaNatural que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la casa con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public PersonaNatural findPersonaNaturalById(Long id) throws SQLException, Exception 
	{
		PersonaNatural personaNatural = null;

		String sql = String.format("SELECT * FROM %1$s.PERSONAS_NAT WHERE ID_OP = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			personaNatural = convertResultSetToPersonaNatural(rs);
		}

		st.close();
		return personaNatural;
	}
	/**
	 * Metodo que agregar la informacion de una nueva personaNatural en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param personaNatural PersonaNatural que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addPersonaNatural(PersonaNatural personaNatural) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.PERSONAS_NAT (ID_OP, APELLIDO, TIPO_DOCUMENTO, NUM_DOCUMENTO, TIPO) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s')", 
				USUARIO, 
				personaNatural.getId(),
				personaNatural.getApellido(),
				personaNatural.getTipoDocumento(),
				personaNatural.getNumDocumento(),
				personaNatural.getTipoPersona());
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion de la personaNatural en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param personaNatural PersonaNatural que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updatePersonaNatural(PersonaNatural personaNatural) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.PERSONA_NAT SET ", USUARIO));
		sql.append(String.format("ID_OP	 = '%1$s' AND APELLIDO = '%2$s' AND TIPO_DOCUMENTO = '%3$s' AND NUM_DOCUMENTO = '%4$s'AND TIPO = '%5$s'",
					personaNatural.getId(),
					personaNatural.getApellido(),
					personaNatural.getTipoDocumento(),
					personaNatural.getNumDocumento(),
					personaNatural.getTipoPersona()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la personaNatural en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param personaNatural PersonaNatural que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deletePersonaNatural(PersonaNatural personaNatural) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.PERSONAS_NAT WHERE ID_OP = %2$d", USUARIO, personaNatural.getId());

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
	public PersonaNatural convertResultSetToPersonaNatural(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID_OP");
		
		DAOOperador daoOp = new DAOOperador();
		daoOp.setConn(conn);
		Operador operador = daoOp.findOperadorById(id);
		
		DAOHabHost daoHab = new DAOHabHost();
		daoHab.setConn(conn);
		ArrayList<HabHost> habsHost= daoHab.getHabsHostOfPersonaNatural(id);
		
		DAOCasa daoCasa = new DAOCasa();
		daoCasa.setConn(conn);
		ArrayList<Casa> casas = daoCasa.getCasasOfPersonaNatural(id);
		
		DAOApartamento daoAp = new DAOApartamento();
		daoAp.setConn(conn);
		ArrayList<Apartamento> apartamentos = daoAp.getApartamentoOfPersonaNatural(id);
		
		String apellido = resultSet.getString("APELLIDO");
		String tipoDocumento = resultSet.getString("TIPO_DOCUMENTO");
		long numDoc = resultSet.getLong("NUM_DOCUMENTO");
		String tipo =resultSet.getString("TIPO");
		
		PersonaNatural personaNatural = new PersonaNatural(id, operador.getNombre(), operador.getTipo(), tipo, apellido, tipoDocumento, numDoc, habsHost, casas, apartamentos);
		
		return personaNatural;
	}
}
