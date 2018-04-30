package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import auxiliary.Fechas;
import auxiliary.RFC1;
import exceptions.BusinessLogicException;
import vos.Alojamiento;
import vos.OfertaAlojamiento;
import vos.Reserva;
import vos.ReservaColectiva;
import vos.Servicio;

public class DAOReservaColectiva {
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
	
	public DAOReservaColectiva() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todas las reservas en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todas las reservas que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<ReservaColectiva> getReservasColectivas() throws SQLException, Exception {
		ArrayList<ReservaColectiva> reservas = new ArrayList<ReservaColectiva>();

		String sql = String.format("SELECT * FROM %1$s.RESERVAS_COLECTIVAS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			reservas.add(convertResultSetToReserva(rs));
		}
		
		st.close();
		return reservas;
	}
	
	/**
	 * Metodo que obtiene la informacion de la reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador de la reserva
	 * @return la informacion de la reserva que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe la reserva con los criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ReservaColectiva findReservaColectivaById(Long id) throws SQLException, Exception 
	{
		
		ReservaColectiva reserva = null;

		String sql = String.format("SELECT * FROM %1$s.RESERVAS_COLECTIVAS WHERE ID = %2$d", USUARIO, id);
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			reserva = convertResultSetToReserva(rs);
		}

		st.close();
		return reserva;
	}
	/**
	 * Metodo que agregar la informacion de una nueva reserva en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param reeserva Reserva que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ReservaColectiva addReserva(ReservaColectiva reserva,  int necesitadas, List<Servicio> servicios, String tipoAloja) throws SQLException, Exception {

		reserva.setId(generateNewId());
		
		reserva.setEstado("RESERVADA");
		
		String sql = String.format("insert INTO %1$s.RESERVAS_COLECTIVAS(ID, EVENTO, ESTADO, FECHA_FIN, FECHA_INICIO, TIPO_CONTRATO, NUM_PERSONAS_POR_RESERVA, ID_CLIENTE) VALUES (%2$s,'%3$s', 'RESERVADA', '%4$s', '%5$s','%6$s', %7$s, %8$s )",
					USUARIO,
					reserva.getId(),
					reserva.getEvento(),
					Fechas.pasarDateAFormatoSQL(reserva.getFechaFin()),
					Fechas.pasarDateAFormatoSQL(reserva.getFechaInicio()),
					reserva.getTipoContrato(),
					reserva.getNumPersonasPorReserva(),
					reserva.getClienteId());
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
		sql = String.format("SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS INNER JOIN (SELECT * FROM %1$s.ALOJAMIENTOS WHERE TIPO = '%2$s'",
				USUARIO,
				tipoAloja);

		if (!servicios.isEmpty()) {

			for(int i = 0; i < reserva.getServicios().size(); i++) {

				String adicion = String.format("AND  EXISTS (SELECT * FROM (SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL)AND NOT EXISTS(SELECT * FROM %1$s.RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s'OR FECHA_INICIO = '%2$s' OR FECHA_FIN = '%3$s') AND ID_AL_OF = %1$s.OFERTAS_ALOJAMIENTOS.ID_AL))DISPONIBLES INNER JOIN %1$s.ALOJ_SERV ON DISPONIBLES.ID_AL = ALOJ_SERV.ID_ALOJ WHERE (ID_AL=ALOJAMIENTOS.ID AND ID_SERV = %4$s))",
						USUARIO, 
						Fechas.pasarDateAFormatoSQL(reserva.getFechaInicio()), 
						Fechas.pasarDateAFormatoSQL(reserva.getFechaFin()),
						servicios.get(i).getId());

				sql = sql + adicion;
			}
		}
		
		sql = sql + ")UNO ON OFERTAS_ALOJAMIENTOS.ID_AL = UNO.ID";
		
		//sentencia que devuelve todos los alojamientos/ofertas qeu cumplen las caracteristicas requeridas
		System.out.println(sql);

		st = conn.createStatement();
		rs = st.executeQuery( sql );

		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		while (rs.next()) {
			java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			Reserva act = new Reserva(reserva.getFechaInicio(), reserva.getFechaFin(), reserva.getTipoContrato(), reserva.getNumPersonasPorReserva(), rs.getDouble("PRECIO"), rs.getLong("ID"), rs.getDate("FECHA_CREACION"), reserva.getClienteId(), "RESERVADA", reserva.getId(), date);	
			reservas.add(act);
		}
		
		int i = 0;
		
		if (reservas.size() < necesitadas) {
			
			throw new Exception("No hay suficientes alojamientos para la reserva colectiva");
		}
		
		while( i < necesitadas) {
			
			Reserva act = reservas.get(i);
			
//			sql = String.format("insert into %1$s.RESERVAS (FECHA_INICIO, FECHA_FIN, TIPO_CONTRATO, NUM_PERSONAS, ID_AL_OF,FECHA_CREACION_OF,ID_CLIENTE, PRECIO_RESERVA, ESTADO, FECHA_CREACION) values ('%2$s', '%3$s', '%4$s', %5$s, %6$s, '%7$s', %8$s, %9$s, 'RESERVADA', CURRENT_DATE)",
//					USUARIO,
//					Fechas.pasarDateAFormatoSQL(act.getFechaInicio()),
//					Fechas.pasarDateAFormatoSQL(act.getFechaFin()),
//					act.getTipoContrato(),
//					act.getNumPersonas(),
//					act.getAlojamiento(),
//					Fechas.pasarDateAFormatoSQL(act.getFechaCreacionOferta()),
//					act.getCliente(),
//					act.getPrecioReserva());
			
			DAOReserva dao = new DAOReserva();
			
			dao.setConn(conn);
			
			dao.addReserva(act);
			
			reserva.getReservas().add(act);
			
			System.out.println(sql);
			
			Statement st2 = conn.createStatement();
			ResultSet rs2 = st.executeQuery( sql );
			i++;
		}	
		
		//ESTE ES EL FOR QUE AGREGUE PARA QUE AGREGUE LOS SERVICIOS A LA TABLA DE RESERV_COL_SERV
		for(int j = 0; j < servicios.size();j++) {

			sql = String.format("insert into %1$s.RESERV_COL_SERV (ID_RESERV_COL, ID_SERV) VALUES (%2$s,%3$s)",
					USUARIO,
					reserva.getId(),
					servicios.get(j).getId());
			
			st = conn.createStatement();
			rs = st.executeQuery( sql );
		}

		st.close();
		return reserva;
	}

	/**
	 * Metodo que actualiza la informacion de una reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param reserva Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateReservaColectiva(ReservaColectiva reserva) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %1s.RESERVAS SET ", USUARIO));
		sql.append(String.format("ID = %1$s, EVENTO = '%2$'",
				reserva.getId(),
				reserva.getEvento()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion de la reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	//No debe existir
	/*public void deleteReservaColectiva(ReservaColectiva reserva) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.RESERVAS_COLECTIVAS WHERE ID = %2$d", USUARIO, reserva.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
		st.close();
	}*/
	
	/**
	 * Metodo que devuelve todas las reservas de una reserva colectiva
	 * @param id de la reserva colectiva
	 * @return la lista de las reservas
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Long> getReservasColectivasWithServicio(long idServicio) throws SQLException, Exception {
		ArrayList<Long> reservas = new ArrayList<Long>();

		String sql = String.format("SELECT * FROM %1$s.RESERV_COL_SERV INNER JOIN RESERVAS_COLECTIVAS ON ID_RESERV_COL = ID WHERE ID_SERV = %2$s", USUARIO, idServicio);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			reservas.add(rs.getLong("ID_RESERV_COL"));
			
		}
		
		st.close();
		return reservas;
	}
	
	public long generateNewId() throws SQLException {
		
		String sql = String.format("SELECT MAX(ID) FROM %1$s.RESERVAS_COLECTIVAS", USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		rs.next();
		
		long idMax = rs.getLong("MAX(ID)");
		
		
		st.close();
		return idMax+1;
	}
	
	public void cancelarReservaColectiva( ReservaColectiva reservaColectiva) throws Exception {
		
		String sql = String.format("UPDATE %1$s.RESERVAS_COLECTIVAS SET ESTADO = 'CANCELADA' WHERE ID = %2$s", USUARIO,reservaColectiva.getId());
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();
		
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
	@SuppressWarnings("deprecation")
	public ReservaColectiva convertResultSetToReserva(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID");

		String evento = resultSet.getString("EVENTO");
		
		DAOReserva daoReserva = new DAOReserva();
		daoReserva.setConn(conn);
		
		DAOServicio daoServicio = new DAOServicio();
		daoServicio.setConn(conn);
		
		ArrayList<Reserva> reservas = daoReserva.getReservasOfReservaColectiva(id);
		
		ArrayList<Servicio> servicios = daoServicio.getServiciosOfReservaColectiva(id);
		
		ArrayList<String> stringsServicios = new ArrayList<String>();
		
		for(int i = 0; i < servicios.size(); i++) {
			
			stringsServicios.add(servicios.get(i).getTipo());
		}
		
		String estado = resultSet.getString("ESTADO");
		
		Date fechaInicio = resultSet.getDate("FECHA_INICIO");
		
		Date fechaFin = resultSet.getDate("FECHA_FIN");
		
		String tipo = resultSet.getString("TIPO_CONTRATO");

		Integer numPersonas = resultSet.getInt("NUM_PERSONAS_POR_RESERVA");
		
		Long cliente = resultSet.getLong("ID_CLIENTE");
		
		ReservaColectiva reserva = new ReservaColectiva(id, evento, reservas, stringsServicios, estado, fechaInicio, fechaFin, tipo, numPersonas, cliente);

		return reserva;
	}
}