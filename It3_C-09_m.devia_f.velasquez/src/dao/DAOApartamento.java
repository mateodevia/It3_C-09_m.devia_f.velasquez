package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.Apartamento;
import vos.HabHost;
import vos.OfertaAlojamiento;
import vos.Operador;
import vos.PersonaNatural;
import vos.Servicio;

public class DAOApartamento {
	
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
	
	public DAOApartamento() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los apartamentos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los apartamentos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Apartamento> getApartamentos() throws SQLException, Exception {
		ArrayList<Apartamento> apartamentos = new ArrayList<Apartamento>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			apartamentos.add(convertResultSetToApartamento(rs));
		}
		
		st.close();
		return apartamentos;
	}
	
	/**
	 * Metodo que obtiene la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del apartamento
	 * @return la informacion del apartamento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el apartamento con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Apartamento findApartamentoById(Long id) throws SQLException, Exception 
	{
		Apartamento apartamento = null;

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTOS INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL = ID  WHERE ID_AL = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			apartamento = convertResultSetToApartamento(rs);
		}

		st.close();
		return apartamento;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo apartamento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.APARTAMENTOS (ID_AL) VALUES (%2$s)", 
									USUARIO, 
									apartamento.getId());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateApartamento(Apartamento apartamento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.APARTAMENTO SET ", USUARIO));
		sql.append(String.format("ID_AL = %1$s",
					apartamento.getId()));
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.APARTAMENTOS WHERE ID_AL = %2$d", USUARIO, apartamento.getId());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que obtiene la informacion de todos los apartamentos de una persona <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas los apartamentos de una persona
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Apartamento> getApartamentoOfPersonaNatural( long id ) throws SQLException, Exception {
		
		ArrayList<Apartamento> apartamentos = new ArrayList<Apartamento>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS WHERE ID_OP = %2$d AND TIPO = 'APARTAMENTO'", USUARIO, id);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			apartamentos.add(convertResultSetToApartamento(rs));
		}
		
		st.close();
		return apartamentos;
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
	public Apartamento convertResultSetToApartamento(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID");
		
		DAOAlojamiento daoAlojamiento = new DAOAlojamiento();
		daoAlojamiento.setConn(conn);
		Alojamiento alojamiento = daoAlojamiento.findAlojamientoById(id);
		
		Apartamento apartamento = new Apartamento(id, alojamiento.getCapacidad(), alojamiento.getCompartida(), alojamiento.getTipoAlojamiento(), alojamiento.getUbicacion(), alojamiento.getServicios(), alojamiento.getOfertas(), alojamiento.getOperador());

		return apartamento;
	}
}
