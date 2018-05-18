package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import auxiliary.Fechas;
import auxiliary.RFC13;
import auxiliary.UsoCliente;
import auxiliary.UsoTipoCliente;
import vos.Cliente;
import vos.Reserva;

public class DAOCliente {
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
	
	public DAOCliente() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los clientes en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los clientes que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Cliente> getClientes() throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT * FROM %1$s.CLIENTES", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		
		st.close();
		return clientes;
	}
	
	/**
	 * Metodo que obtiene la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del alojamiento
	 * @return la informacion del alojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el alojamiento con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Cliente findClienteById(Long id) throws SQLException, Exception 
	{
		Cliente cliente = null;

		String sql = String.format("SELECT * FROM %1$s.CLIENTES WHERE CARNET_UNIANDES = %2$d", USUARIO, id); 

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			cliente = convertResultSetToCliente(rs);
		}

		st.close();
		return cliente;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo cliente en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addCliente(Cliente cliente) throws SQLException, Exception {

		String fecha = Fechas.pasarDateAFormatoSQL(cliente.getFechaCreacion());
		
		String sql = String.format("INSERT INTO %1$s.CLIENTES (CARNET_UNIANDES, NOMBRE, APELLIDO, TIPO_DOCUMENTO, NUM_DOCUMENTO, TIPO_CLIENTE, FECHA_CREACION) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s', '%8$s')", 
									USUARIO, 
									cliente.getCarnetUniandes(),
									cliente.getNombre(),
									cliente.getApellido(),
									cliente.getTipoDocumento(),
									cliente.getNumDocumento(),
									cliente.getTipoCliente(),
									fecha);
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param cliente Cliente que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateCliente(Cliente cliente) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.ALOJAMIENTOS SET ", USUARIO));
		sql.append(String.format("CARNET_UNIANDES = '%1$s' AND NOMBRE = '%2$s' AND APELLIDO = '%3$s' AND TIPO_DOCUMENTO = '%4$s'AND NUM_DOCUMENTO = '%5$s' AND TIPO_CLIENTE = '%6%s'",
					cliente.getCarnetUniandes(),
					cliente.getNombre(),
					cliente.getApellido(),
					cliente.getTipoDocumento(),
					cliente.getNumDocumento(),
					cliente.getTipoCliente()));
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param cliente Cliente que desea actualizar a la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteCliente(Cliente cliente) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CLIENTES WHERE CARNET_UNIANDES = %2$d", USUARIO, cliente.getCarnetUniandes());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	public long generateNewId() throws SQLException {
		
		String sql = String.format("SELECT MAX(CARNET_UNIANDES) FROM %1$s.CLIENTES", USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		rs.next();
		
		long idMax = rs.getLong("MAX(CARNET_UNIANDES)");
		
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
	 * @throws Exception  si hay un error revisando las reservas del cliente
	 */
	public Cliente convertResultSetToCliente(ResultSet resultSet) throws Exception {
		
		long carnetUniandes = resultSet.getLong("CARNET_UNIANDES");
		String nombre = resultSet.getString("NOMBRE");
		String apellido = resultSet.getString("APELLIDO");
		String tipoDoc = resultSet.getString("TIPO_DOCUMENTO");
		long numDoc = resultSet.getLong("NUM_DOCUMENTO");
		String tipoCliente = resultSet.getString("TIPO_CLIENTE");
		Date fechaCreacion = resultSet.getDate("FECHA_CREACION");
		
		DAOReserva daoReserva = new DAOReserva();
		daoReserva.setConn(conn);
		ArrayList<Reserva> reservas = daoReserva.getReservasOfCliente(carnetUniandes);
		
		
		Cliente cliente = new Cliente(carnetUniandes, nombre, apellido, tipoDoc, numDoc, reservas, tipoCliente, fechaCreacion);

		return cliente;
	}
	

	
	public List<UsoCliente> getUsoCliente(Long idCliente) throws Exception{
		
		String sql = String.format("SELECT ID, SUM(FECHA_FIN - FECHA_INICIO) AS DIAS_CONTRATADOS, SUM(PRECIO_RESERVA) AS DINERO_PAGADO FROM %1$s.RESERVAS JOIN %1$s.ALOJAMIENTOS ON %1$s.RESERVAS.ID_AL_OF = %1$s.ALOJAMIENTOS.ID WHERE %1$s.RESERVAS.FECHA_FIN < CURRENT_DATE AND %1$s.RESERVAS.ESTADO <> 'CANCELADA' AND %1$s.RESERVAS.ID_CLIENTE = %2$s GROUP BY ID", 
				USUARIO, idCliente);
		
		System.out.println("SENTENCIA SQL "+ sql);
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(sql);
		
		
		ArrayList<UsoCliente> respuesta = new ArrayList<UsoCliente>();
		
		Long idAl;
		Integer diasContratados;
		Double dineroPagado;
		
		DAOAlojamiento daoAl = new DAOAlojamiento();
		daoAl.setConn(conn);
		while(rs.next()) {
			idAl = rs.getLong("ID");
			diasContratados = rs.getInt("DIAS_CONTRATADOS");
			dineroPagado = rs.getDouble("DINERO_PAGADO");
			
			respuesta.add(new UsoCliente(daoAl.findAlojamientoById(idAl), diasContratados, dineroPagado, idCliente));
		}
		daoAl.cerrarRecursos();
		st.close();
		
		return respuesta;
	}
	
	public List<UsoTipoCliente> getUsoTipoCliente() throws Exception{
		
		
		String sql = String.format("SELECT TIPO_CLIENTE, %1$s.ALOJAMIENTOS.ID, SUM(FECHA_FIN - FECHA_INICIO) AS DIAS_CONTRATADOS, SUM(PRECIO_RESERVA) AS DINERO_PAGADO FROM %1$s.CLIENTES INNER JOIN %1$s.RESERVAS ON %1$s.RESERVAS.ID_CLIENTE = %1$s.CLIENTES.CARNET_UNIANDES INNER JOIN %1$s.ALOJAMIENTOS ON %1$s.RESERVAS.ID_AL_OF = %1$s.ALOJAMIENTOS.ID GROUP BY TIPO_CLIENTE, %1$s.ALOJAMIENTOS.ID ORDER BY TIPO_CLIENTE", 
				USUARIO);
		
		System.out.println("SENTENCIA SQL "+ sql);
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery(sql);
		
		
		ArrayList<UsoTipoCliente> respuesta = new ArrayList<UsoTipoCliente>();
		
		Long idAl;
		Integer diasContratados;
		Double dineroPagado;
		String tipoCliente;
		
		DAOAlojamiento daoAl = new DAOAlojamiento();
		daoAl.setConn(conn);
		while(rs.next()) {
			idAl = rs.getLong("ID");
			diasContratados = rs.getInt("DIAS_CONTRATADOS");
			dineroPagado = rs.getDouble("DINERO_PAGADO");
			tipoCliente = rs.getString("TIPO_CLIENTE");
			
			respuesta.add(new UsoTipoCliente(tipoCliente, diasContratados, dineroPagado, daoAl.findAlojamientoById(idAl)));
		}
		daoAl.cerrarRecursos();
		st.close();
		
		return respuesta;
	}
	

	
	public List<Cliente> getClientesFrecuentesOfAl(Long idAl) throws Exception{
		
		List<Cliente> respuesta = new ArrayList<Cliente>();

		String sql = String.format("SELECT * FROM %1$s.CLIENTES INNER JOIN (SELECT ID_AL_OF, ID_CLIENTE, COUNT(*) AS NUM_RESERVAS, SUM(FECHA_FIN-FECHA_INICIO) AS NUM_DIAS FROM %1$s.RESERVAS WHERE ID_AL_OF = 10 GROUP BY ID_CLIENTE, ID_AL_OF HAVING SUM(FECHA_FIN-FECHA_INICIO) >= 15 OR COUNT(*) >=3) ON %1$s.CLIENTES.CARNET_UNIANDES = ID_CLIENTE",
				USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );

		while (rs.next()) {
			respuesta.add(convertResultSetToCliente(rs));
		}
		
		st.close();
		return respuesta;	
	}
	
	public List<RFC13> getBuenosClientes() throws Exception{
		
		List<RFC13> respuesta = new ArrayList<RFC13>();
		
		String sql = "SELECT * FROM (\r\n" + 
				"    SELECT CLIENTES.*, (CASE  WHEN CARNET_UNIANDES IN ( SELECT MESES_RESERVADOS.CARNET_UNIANDES\r\n" + 
				"                                                        FROM(   SELECT CLIENTES.CARNET_UNIANDES,TRUNC( MONTHS_BETWEEN( CURRENT_DATE,CLIENTES.FECHA_CREACION ) ) as MESES\r\n" + 
				"                                                                FROM CLIENTES\r\n" + 
				"                                                            ) MESES_CREACION\r\n" + 
				"                                                        INNER JOIN\r\n" + 
				"                                                            (   SELECT CLIENTES.CARNET_UNIANDES, COUNT(DISTINCT (EXTRACT(MONTH FROM RESERVAS.FECHA_INICIO) || '/' || EXTRACT (YEAR FROM RESERVAS.FECHA_INICIO))) AS MESES\r\n" + 
				"                                                                FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES\r\n" + 
				"                                                                WHERE RESERVAS.FECHA_INICIO <= CURRENT_DATE \r\n" + 
				"                                                                GROUP BY CLIENTES.CARNET_UNIANDES\r\n" + 
				"                                                            ) MESES_RESERVADOS\r\n" + 
				"                                                        ON MESES_RESERVADOS.CARNET_UNIANDES = MESES_CREACION.CARNET_UNIANDES\r\n" + 
				"                                                        WHERE MESES_CREACION.MESES = MESES_RESERVADOS.MESES\r\n" + 
				"                                                      ) THEN 0\r\n" + 
				"                \r\n" + 
				"                              WHEN CARNET_UNIANDES IN (    SELECT CLIENTES.CARNET_UNIANDES \r\n" + 
				"                                                           FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF\r\n" + 
				"                                                           GROUP BY CLIENTES.CARNET_UNIANDES\r\n" + 
				"\r\n" + 
				"                                                           MINUS\r\n" + 
				"\r\n" + 
				"                                                           SELECT CLIENTES.CARNET_UNIANDES\r\n" + 
				"                                                           FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN OFERTAS_ALOJAMIENTOS ON OFERTAS_ALOJAMIENTOS.ID_AL = RESERVAS.ID_AL_OF\r\n" + 
				"                                                           WHERE OFERTAS_ALOJAMIENTOS.PRECIO > 433000\r\n" + 
				"                \r\n" + 
				"                                                      ) THEN 1\r\n" + 
				"                                                      \r\n" + 
				"                              WHEN CARNET_UNIANDES IN (   SELECT CLIENTES.CARNET_UNIANDES \r\n" + 
				"                                                          FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF\r\n" + 
				"                                                          GROUP BY CLIENTES.CARNET_UNIANDES\r\n" + 
				"\r\n" + 
				"                                                          MINUS\r\n" + 
				"\r\n" + 
				"                                                          SELECT CLIENTES.CARNET_UNIANDES\r\n" + 
				"                                                          FROM CLIENTES INNER JOIN RESERVAS ON RESERVAS.ID_CLIENTE = CLIENTES.CARNET_UNIANDES INNER JOIN HABS_HOTELES ON HABS_HOTELES.ID_AL = RESERVAS.ID_AL_OF\r\n" + 
				"                                                          WHERE TIPO = 'SEMISUITE' OR TIPO = 'ESTANDAR'\r\n" + 
				"                                                      ) THEN 2\r\n" + 
				"                        END) AS JUSTIFICACION\r\n" + 
				"    FROM CLIENTES)\r\n" + 
				"WHERE JUSTIFICACION IS NOT NULL";
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
		while(rs.next()) {
			respuesta.add(new RFC13(convertResultSetToCliente(rs), rs.getInt("JUSTIFICACION")));
		}
		
		st.close();
		return respuesta;
	}
}
