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
import auxiliary.Log;
import auxiliary.RFC1;
import tipo.TipoAlojamiento;
import tipo.TipoUnidadTiempo;
import vos.Reserva;

public class DAOReserva {
	// ----------------------------------------------------------------------------------------------
	// Constantes
	// ----------------------------------------------------------------------------------------------

	public final static String FELIPE = "ISIS2304A1001810";
	public final static String MATEO = "ISIS2304A821810";

	public final static String USUARIO = FELIPE;

	// ----------------------------------------------------------------------------------------------
	// Atributos
	// ----------------------------------------------------------------------------------------------

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	// ----------------------------------------------------------------------------------------------
	// Constructor
	// ----------------------------------------------------------------------------------------------

	public DAOReserva() {
		recursos = new ArrayList<Object>();
	}

	// ----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	// ----------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todas las reservas en la Base de Datos
	 * <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @return lista con la informacion de todas las reservas que se encuentran en
	 *         la Base de Datos
	 * @throws SQLException
	 *             Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReservas() throws SQLException, Exception {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = String.format("SELECT * FROM %1$s.RESERVAS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			reservas.add(convertResultSetToReserva(rs));
		}

		st.close();
		return reservas;
	}

	/**
	 * Metodo que obtiene la informacion de la reserva en la Base de Datos que tiene
	 * el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param id
	 *            el identificador de la reserva
	 * @return la informacion de la reserva que cumple con los criterios de la
	 *         sentecia SQL Null si no existe la reserva con los criterios
	 *         establecidos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public Reserva findReservaById(Long idAl, Date fechaInicio, Date fechaCreacionOferta)
			throws SQLException, Exception {
		Reserva reserva = null;

		String sql = String.format(
				"SELECT * FROM %1$s.RESERVAS WHERE FECHA_INICIO = '%2$s' AND ID_AL_OF = %3$d AND FECHA_CREACION_OF = '%4$s'",
				USUARIO, Fechas.pasarDateAFormatoSQL(fechaInicio), idAl,
				Fechas.pasarDateAFormatoSQL(fechaCreacionOferta));

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			reserva = convertResultSetToReserva(rs);
		}

		return reserva;
	}

	/**
	 * Metodo que agregar la informacion de una nueva reserva en la Base de Datos a
	 * partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param reeserva
	 *            Reserva que desea agregar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void addReserva(Reserva reserva) throws SQLException, Exception {

		String fechaInicio = "" + reserva.getFechaInicio().getDate() + "/" + (reserva.getFechaInicio().getMonth() + 1)
				+ "/" + (reserva.getFechaInicio().getYear() - 100);

		String fechaFin = "" + reserva.getFechaFin().getDate() + "/" + (reserva.getFechaFin().getMonth() + 1) + "/"
				+ (reserva.getFechaFin().getYear() - 100);

		String fechaCreacionOF = "" + reserva.getFechaCreacionOferta().getDate() + "/"
				+ (reserva.getFechaCreacionOferta().getMonth() + 1) + "/"
				+ (reserva.getFechaCreacionOferta().getYear() - 100);

		String sql = String.format(
				"INSERT INTO %1$s.RESERVAS (FECHA_INICIO, FECHA_FIN, TIPO_CONTRATO, NUM_PERSONAS, ID_AL_OF,FECHA_CREACION_OF,ID_CLIENTE, PRECIO_RESERVA, ESTADO, FECHA_CREACION, ID_COLECTIVA) values ('%2$s', '%3$s', '%4$s', %5$s, %6$s, '%7$s', %8$s, %9$s, '%10$s', CURRENT_DATE, %11$s)",
				USUARIO, fechaInicio, fechaFin, reserva.getTipoContrato(), reserva.getNumPersonas(),
				reserva.getAlojamiento(), fechaCreacionOF, reserva.getCliente(), reserva.getPrecioReserva(),
				reserva.getEstado(), reserva.getReservaColectiva());
		System.out.println(sql);

		conn.setAutoCommit(true);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		//actualiza la tabla nueva

		sql = "select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin \r\n" + 
				"from reservas\r\n" + 
				"where id_al_of = "+reserva.getAlojamiento()+" and fecha_inicio = '"+fechaInicio+"' and fecha_creacion_of = '"+fechaCreacionOF+"'";

		rs = st.executeQuery(sql);

		rs.next();

		Log rpta = new Log(rs.getString("ID_AL_OF")+","+
				rs.getString("SEMANA_INICIO")+","+
				rs.getString("ANIO_INICIO")+","+
				rs.getString("SEMANA_FIN")+","+
				rs.getString("ANIO_FIN"));

		String act = rpta.getMsg();

		String[] actuales = act.split(",");

		sql = "select * from alojamientos where id = "+ actuales[0];


		rs = st.executeQuery(sql);

		rs.next();

		String operador = rs.getString("ID_OP");

		//si los años son iguales
		if(actuales[2].equals(actuales[4])) {

			//por cada semana desde el inicio hasta el final
			for(int j = Integer.parseInt(actuales[1]); j <= Integer.parseInt(actuales[3]);j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);


				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[2] + ",1, "+ operador+")";
					st.executeQuery(sql);
				}
			}
		}
		else {

			//todas las semanas hasta que se acabe la actual
			for(int j = Integer.parseInt(actuales[1]); j <= 52; j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);			

				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[2] + ",1,"+ operador+")";
					st.executeQuery(sql);
				}
			}

			//para todos los años entre el primero y el ultimo
			for(int j = Integer.parseInt(actuales[2])+1;j<Integer.parseInt(actuales[4]); j++ ) {

				//para todas las semanas del año
				for(int k = 1; k<=52;k++) {

					sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+k+"and anio = " + j+" and id_aloj = " + actuales[0];
					rs = st.executeQuery(sql);			

					if(rs.next()){

						int numReservas = rs.getInt("NUM_RESERV");

						numReservas++;

						sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+k+" and anio = " + j+"and id_aloj = " + actuales[0];

						st.executeQuery(sql);
					}
					else {

						sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + k + "," + j + ",1, "+ operador+")";
						st.executeQuery(sql);

					}
				}
			}

			//para cada semana desde la primera del ultimo año hasta la ultima
			for(int j = 1; j<=Integer.parseInt(actuales[3]); j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[4]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);			

				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[4]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[4] + ",1, "+ operador+")";
					st.executeQuery(sql);

				}
			}
		}

		st.close();

	}

	/**
	 * Metodo que actualiza la informacion de una reserva en la Base de Datos que
	 * tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param reserva
	 *            Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void updateReserva(Reserva reserva) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();

		sql.append(String.format("UPDATE %s.RESERVAS SET ", USUARIO));
		sql.append(String.format(
				"FECHA_INICIO = '%1$s' , FECHA_FIN = '%2$s' , TIPO_CONTRATO = '%3$s' , NUM_PERSONAS = %4$s , ID_CLIENTE = %5$s , ID_AL_OF = %6$s , FECHA_CREACION_OF = '%7$s' , PRECIO_RESERVA = %8$s , ESTADO = '%9$s', FECHA_CREACION = '%10$s', ID_COLECTIVA = %11$s WHERE FECHA_INICIO = '%1$s' AND ID_AL_OF = %6$s AND FECHA_CREACION_OF = '%7$s'",
				Fechas.pasarDateAFormatoSQL(reserva.getFechaInicio()),
				Fechas.pasarDateAFormatoSQL(reserva.getFechaFin()), reserva.getTipoContrato(), reserva.getNumPersonas(),
				reserva.getCliente(), reserva.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(reserva.getFechaCreacionOferta()), reserva.getPrecioReserva(),
				reserva.getEstado(), Fechas.pasarDateAFormatoSQL(reserva.getFechaCreacion()),
				reserva.getReservaColectiva()));

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql.toString());

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		st.close();
	}

	/**
	 * Metodo que borra la informacion de la reserva en la Base de Datos que tiene
	 * el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param alojamiento
	 *            Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */

	// MÃ©todo no tiene razÃ³n de existir
	/*
	 * public void deleteReserva(Reserva reserva) throws SQLException, Exception {
	 * 
	 * String sql = String.format("DELETE FROM %1$s.RESERVAS WHERE ID = %2$d",
	 * USUARIO, reserva.getId());
	 * 
	 * System.out.println(sql);
	 * 
	 * Statement st = conn.createStatement(); ResultSet rs = st.executeQuery( sql );
	 * 
	 * // PreparedStatement prepStmt = conn.prepareStatement(sql); //
	 * recursos.add(prepStmt); // ResultSet rs = prepStmt.executeQuery();
	 * 
	 * st.close(); }
	 */

	/**
	 * Metodo que devuelve todas las reservas de una oferta
	 * 
	 * @param id
	 *            de la oferta
	 * @return la lista de las reservas
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReservasOfOfertaAlojamiento(long id) throws SQLException, Exception {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = String.format("SELECT * FROM %1$s.RESERVAS WHERE ID_AL_OF = %2$s", USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {

			reservas.add(convertResultSetToReserva(rs));

		}

		st.close();
		return reservas;
	}

	public Reserva getLastReservaOfOfertaAlojamiento(long id, Date fCreacion) throws Exception {
		String sql = String.format(
				"SELECT * FROM %1$s.RESERVAS R INNER JOIN (SELECT MAX(FECHA_FIN)AS FECHA FROM %1$s.RESERVAS)M ON R.FECHA_FIN = M.FECHA WHERE ID_Al_OF = %2$s AND FECHA_CREACION = '%3$s'",
				USUARIO, id, Fechas.pasarDateAFormatoSQL(fCreacion));
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		if (rs.next()) {
			st.close();
			return convertResultSetToReserva(rs);
		} else {
			st.close();
			return null;
		}
	}

	/**
	 * Metodo que devuelve todas las reservas de un cliente
	 * 
	 * @param carnet
	 *            del cliente
	 * @return la lista de las reservas
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReservasOfCliente(long carnet) throws SQLException, Exception {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = String.format("SELECT * FROM %1$s.RESERVAS WHERE ID_CLIENTE = %2$s", USUARIO, carnet);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {

			reservas.add(convertResultSetToReserva(rs));

		}

		st.close();
		return reservas;
	}

	/**
	 * Metodo que devuelve todas las reservas de una reserva colectiva
	 * 
	 * @param id
	 *            de la reserva colectiva
	 * @return la lista de las reservas
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReservasOfReservaColectiva(long idColectiva) throws SQLException, Exception {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		System.out.println("CONN " + conn);

		String sql = String.format("SELECT * FROM %1$s.RESERVAS WHERE ID_COLECTIVA = %2$s", USUARIO, idColectiva);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {

			reservas.add(convertResultSetToReserva(rs));

		}
		return reservas;
	}

	// Recibe los strings en formato dd/mm/aa.
	public List<RFC1> getDineroDates(String DateInicio, String DateFin) throws Exception {

		List<RFC1> lista = new ArrayList<RFC1>();

		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(String.
		 * format("SELECT IDOP_RESERVA.ID_OP, SUM(IDOP_RESERVA.PRECIO_RESERVA) AS DINERO_OBTENIDO"
		 * )); sql.append(String.
		 * format("FROM (SELECT IDOP_IDOF.ID_OP, RESERVAS.ID, RESERVAS.FECHA_INICIO, RESERVAS.FECHA_FIN, RESERVAS.PRECIO_RESERVA"
		 * ));
		 * sql.append(String.format("FROM (SELECT ALOJ.ID_OP, OFERTAS.ID AS ID_OFERTA"))
		 * ; sql.append(String.format(" FROM (SELECT * FROM %1$s.ALOJAMIENTOS) ALOJ",
		 * USUARIO)); sql.append(String.format("INNER JOIN"));
		 */

		String sql = String.format(
				"SELECT ID_OP, SUM (DINERO_AL) AS DINERO_OBTENIDO FROM %1$s.ALOJAMIENTOS RIGHT OUTER JOIN(SELECT ID_AL, SUM(%1$s.RESERVAS.PRECIO_RESERVA) AS DINERO_AL FROM (%1$s.RESERVAS INNER JOIN %1$s.OFERTAS_ALOJAMIENTOS OA ON %1$s.RESERVAS.ID_AL_OF = OA.ID_AL AND %1$s.RESERVAS.FECHA_CREACION_OF = OA.FECHA_CREACION) WHERE FECHA_FIN > '%2$s' AND FECHA_FIN < '%3$s'GROUP BY ID_AL) IDAL_DIN ON IDAL_DIN.ID_AL = %1$s.ALOJAMIENTOS.ID GROUP BY ID_OP",
				USUARIO, DateInicio, DateFin);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		System.out.println("SQL " + sql);

		while (rs.next()) {
			lista.add(new RFC1(rs.getLong("ID_OP"), rs.getDouble("DINERO_OBTENIDO")));
		}

		st.close();
		return lista;
	}

	public void cancelarReserva(Reserva reserva, double multa) throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql.append(String.format("UPDATE %s.RESERVAS SET ", USUARIO));
		sql.append(String.format(
				"ESTADO = 'CANCELADA', PRECIO_RESERVA = %1$s WHERE FECHA_CREACION_OF = '%2$s' AND ID_AL_OF = %3$s AND FECHA_INICIO = '%4$s'",
				multa, Fechas.pasarDateAFormatoSQL(reserva.getFechaCreacionOferta()), reserva.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(reserva.getFechaInicio())));

		System.out.println(sql);

		conn.setAutoCommit(true);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql.toString());

		//actualiza la tabla nueva

		String fechaInicio = "" + reserva.getFechaInicio().getDate() + "/" + (reserva.getFechaInicio().getMonth() + 1)
				+ "/" + (reserva.getFechaInicio().getYear() - 100);

		String fechaFin = "" + reserva.getFechaFin().getDate() + "/" + (reserva.getFechaFin().getMonth() + 1) + "/"
				+ (reserva.getFechaFin().getYear() - 100);

		String fechaCreacionOF = "" + reserva.getFechaCreacionOferta().getDate() + "/"
				+ (reserva.getFechaCreacionOferta().getMonth() + 1) + "/"
				+ (reserva.getFechaCreacionOferta().getYear() - 100);

		String	sql2 = "select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin \r\n" + 
				"from reservas\r\n" + 
				"where id_al_of = "+reserva.getAlojamiento()+" and fecha_inicio = '"+fechaInicio+"' and fecha_creacion_of = '"+fechaCreacionOF+"'";

		rs = st.executeQuery(sql2);

		rs.next();

		Log rpta = new Log(rs.getString("ID_AL_OF")+","+
				rs.getString("SEMANA_INICIO")+","+
				rs.getString("ANIO_INICIO")+","+
				rs.getString("SEMANA_FIN")+","+
				rs.getString("ANIO_FIN"));

		String act = rpta.getMsg();

		String[] actuales = act.split(",");

		sql2 = "select * from alojamientos where id = "+ actuales[0];


		rs = st.executeQuery(sql2);

		rs.next();

		String operador = rs.getString("ID_OP");

		//si los años son iguales
		if(actuales[2].equals(actuales[4])) {

			//por cada semana desde el inicio hasta el final
			for(int j = Integer.parseInt(actuales[1]); j <= Integer.parseInt(actuales[3]);j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql2);

				if(rs.next()) {

					int numReservas = rs.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}
		else {

			//todas las semanas hasta que se acabe la actual
			for(int j = Integer.parseInt(actuales[1]); j <= 52; j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql2);			

				if(rs.next()) {

					int numReservas = rs.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}

		//para todos los años entre el primero y el ultimo
		for(int j = Integer.parseInt(actuales[2])+1;j<Integer.parseInt(actuales[4]); j++ ) {

			//para todas las semanas del año
			for(int k = 1; k<=52;k++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+k+"and anio = " + j+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql2);			

				if(rs.next()) {

					int numReservas = rs.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}

		//para cada semana desde la primera del ultimo año hasta la ultima
		for(int j = 1; j<=Integer.parseInt(actuales[3]); j++) {

			sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[4]+" and id_aloj = " + actuales[0];
			ResultSet rs2 = st.executeQuery(sql2);			

			if(rs2.next()){

				int numReservas = rs2.getInt("NUM_RESERV");

				if(numReservas>1){

					numReservas--;

					sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql2);
				}
				else {

					sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
					st.executeQuery(sql2);
				}
			}
		}
		st.close();
	}



	public void cancelarReservaSinCommit(Reserva reserva, double multa) throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql.append(String.format("UPDATE %s.RESERVAS SET ", USUARIO));
		sql.append(String.format(
				"ESTADO = 'CANCELADA', PRECIO_RESERVA = %1$s WHERE FECHA_CREACION_OF = '%2$s' AND ID_AL_OF = %3$s AND FECHA_INICIO = '%4$s'",
				multa, Fechas.pasarDateAFormatoSQL(reserva.getFechaCreacionOferta()), reserva.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(reserva.getFechaInicio())));

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql.toString());

		//actualiza la tabla nueva

		String fechaInicio = "" + reserva.getFechaInicio().getDate() + "/" + (reserva.getFechaInicio().getMonth() + 1)
				+ "/" + (reserva.getFechaInicio().getYear() - 100);

		String fechaFin = "" + reserva.getFechaFin().getDate() + "/" + (reserva.getFechaFin().getMonth() + 1) + "/"
				+ (reserva.getFechaFin().getYear() - 100);

		String fechaCreacionOF = "" + reserva.getFechaCreacionOferta().getDate() + "/"
				+ (reserva.getFechaCreacionOferta().getMonth() + 1) + "/"
				+ (reserva.getFechaCreacionOferta().getYear() - 100);

		String	sql2 = "select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin \r\n" + 
				"from reservas\r\n" + 
				"where id_al_of = "+reserva.getAlojamiento()+" and fecha_inicio = '"+fechaInicio+"' and fecha_creacion_of = '"+fechaCreacionOF+"'";

		rs = st.executeQuery(sql2);

		rs.next();

		Log rpta = new Log(rs.getString("ID_AL_OF")+","+
				rs.getString("SEMANA_INICIO")+","+
				rs.getString("ANIO_INICIO")+","+
				rs.getString("SEMANA_FIN")+","+
				rs.getString("ANIO_FIN"));

		String act = rpta.getMsg();

		String[] actuales = act.split(",");

		sql2 = "select * from alojamientos where id = "+ actuales[0];


		rs = st.executeQuery(sql2);

		rs.next();

		String operador = rs.getString("ID_OP");

		//si los años son iguales
		if(actuales[2].equals(actuales[4])) {

			//por cada semana desde el inicio hasta el final
			for(int j = Integer.parseInt(actuales[1]); j <= Integer.parseInt(actuales[3]);j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}
		else {

			//todas las semanas hasta que se acabe la actual
			for(int j = Integer.parseInt(actuales[1]); j <= 52; j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}

		//para todos los años entre el primero y el ultimo
		for(int j = Integer.parseInt(actuales[2])+1;j<Integer.parseInt(actuales[4]); j++ ) {

			//para todas las semanas del año
			for(int k = 1; k<=52;k++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+k+"and anio = " + j+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}

			}
		}

		//para cada semana desde la primera del ultimo año hasta la ultima
		for(int j = 1; j<=Integer.parseInt(actuales[3]); j++) {

			sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[4]+" and id_aloj = " + actuales[0];
			ResultSet rs2 = st.executeQuery(sql2);			

			if(rs2.next()) {

				int numReservas = rs2.getInt("NUM_RESERV");

				if(numReservas>1){

					numReservas--;

					sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql2);
				}
				else {

					sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
					st.executeQuery(sql2);
				}
			}
		}
		st.close();
	}

	// ----------------------------------------------------------------------------------------------
	// Metodos Auxiliares
	// ----------------------------------------------------------------------------------------------

	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a
	 * partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * 
	 * @param connection
	 *            la conexion generada en el TransactionManager para la comunicacion
	 *            con la Base de Datos
	 */
	public void setConn(Connection connection) {
		this.conn = connection;
	}

	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de
	 * recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido
	 * cerrados.
	 */
	public void cerrarRecursos() {
		for (Object ob : recursos) {
			if (ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL en una
	 * instancia java.
	 * 
	 * @param resultSet
	 *            ResultSet que se obtuvo de la base de datos.
	 * @return Alojamiento cuyos atributos corresponden a los valores asociados a un
	 *         registro particular de la tabla.
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Reserva convertResultSetToReserva(ResultSet resultSet) throws Exception {
		Date fechaInicio = resultSet.getDate("FECHA_INICIO");

		Date fechaFin = resultSet.getDate("FECHA_FIN");
		// -----------------------------------------------------------------------
		String tipoContrato = resultSet.getString("TIPO_CONTRATO");

		int numPersonas = resultSet.getInt("NUM_PERSONAS");

		double precioReserva = resultSet.getDouble("PRECIO_RESERVA");

		long idCliente = resultSet.getLong("ID_CLIENTE");

		long idOferta = resultSet.getLong("ID_AL_OF");

		String estado = resultSet.getString("ESTADO");

		long reservaColectiva = resultSet.getLong("ID_COLECTIVA");

		Date fechaCreacion = resultSet.getDate("FECHA_CREACION");

		Date fechaCreacionOferta = resultSet.getDate("FECHA_CREACION_OF");

		Reserva reserva = new Reserva(fechaInicio, fechaFin, tipoContrato, numPersonas, precioReserva, idOferta,
				fechaCreacionOferta, idCliente, estado, reservaColectiva, fechaCreacion);

		return reserva;
	}

	public void cancelarReservaD(Reserva r) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append(String.format("UPDATE %s.RESERVAS SET ", USUARIO));
		sql.append(String.format(
				"ESTADO = 'CANCELADA', PRECIO_RESERVA = 0.0 WHERE FECHA_CREACION_OF = '%1$s' AND ID_AL_OF = %2$s AND FECHA_INICIO = '%3$s'",
				Fechas.pasarDateAFormatoSQL(r.getFechaCreacionOferta()), r.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(r.getFechaInicio())));

		System.out.println(sql);

		conn.setAutoCommit(false);
		Statement st = conn.createStatement();
		st.executeQuery(sql.toString());

		//actualiza la tabla nueva

		String fechaInicio = "" + r.getFechaInicio().getDate() + "/" + (r.getFechaInicio().getMonth() + 1)
				+ "/" + (r.getFechaInicio().getYear() - 100);

		String fechaFin = "" + r.getFechaFin().getDate() + "/" + (r.getFechaFin().getMonth() + 1) + "/"
				+ (r.getFechaFin().getYear() - 100);

		String fechaCreacionOF = "" + r.getFechaCreacionOferta().getDate() + "/"
				+ (r.getFechaCreacionOferta().getMonth() + 1) + "/"
				+ (r.getFechaCreacionOferta().getYear() - 100);

		String	sql2 = "select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin \r\n" + 
				"from reservas\r\n" + 
				"where id_al_of = "+r.getAlojamiento()+" and fecha_inicio = '"+fechaInicio+"' and fecha_creacion_of = '"+fechaCreacionOF+"'";

		ResultSet rs = st.executeQuery(sql2);

		rs.next();

		Log rpta = new Log(rs.getString("ID_AL_OF")+","+
				rs.getString("SEMANA_INICIO")+","+
				rs.getString("ANIO_INICIO")+","+
				rs.getString("SEMANA_FIN")+","+
				rs.getString("ANIO_FIN"));

		String act = rpta.getMsg();

		String[] actuales = act.split(",");

		sql2 = "select * from alojamientos where id = "+ actuales[0];


		rs = st.executeQuery(sql2);

		rs.next();

		String operador = rs.getString("ID_OP");

		//si los años son iguales
		if(actuales[2].equals(actuales[4])) {

			//por cada semana desde el inicio hasta el final
			for(int j = Integer.parseInt(actuales[1]); j <= Integer.parseInt(actuales[3]);j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}
		else {

			//todas las semanas hasta que se acabe la actual
			for(int j = Integer.parseInt(actuales[1]); j <= 52; j++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}
			}
		}

		//para todos los años entre el primero y el ultimo
		for(int j = Integer.parseInt(actuales[2])+1;j<Integer.parseInt(actuales[4]); j++ ) {

			//para todas las semanas del año
			for(int k = 1; k<=52;k++) {

				sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+k+"and anio = " + j+" and id_aloj = " + actuales[0];
				ResultSet rs2 = st.executeQuery(sql2);			

				if(rs2.next()) {

					int numReservas = rs2.getInt("NUM_RESERV");

					if(numReservas>1){

						numReservas--;

						sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

						st.executeQuery(sql2);
					}
					else {

						sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
						st.executeQuery(sql2);
					}
				}

			}
		}

		//para cada semana desde la primera del ultimo año hasta la ultima
		for(int j = 1; j<=Integer.parseInt(actuales[3]); j++) {

			sql2 = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[4]+" and id_aloj = " + actuales[0];
			ResultSet rs2 = st.executeQuery(sql2);			

			if(rs2.next()) {

				int numReservas = rs2.getInt("NUM_RESERV");

				if(numReservas>1){

					numReservas--;

					sql2 = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql2);
				}
				else {

					sql2 = "delete from  NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
					st.executeQuery(sql2);
				}
			}
		}
		st.close();

	}

	public void addReservaD(Reserva r) throws SQLException {

		String sql = String.format(
				"INSERT INTO %1$s.RESERVAS (FECHA_INICIO, FECHA_FIN, TIPO_CONTRATO, NUM_PERSONAS, ID_AL_OF,FECHA_CREACION_OF,ID_CLIENTE, PRECIO_RESERVA, ESTADO, FECHA_CREACION, ID_COLECTIVA) values ('%2$s', '%3$s', '%4$s', %5$s, %6$s, '%7$s', %8$s, %9$s, '%10$s', '%12$s', %11$s)",
				USUARIO, Fechas.pasarDateAFormatoSQL(r.getFechaInicio()), Fechas.pasarDateAFormatoSQL(r.getFechaFin()),
				r.getTipoContrato(), r.getNumPersonas(), r.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(r.getFechaCreacionOferta()), r.getCliente(), r.getPrecioReserva(),
				r.getEstado(), r.getReservaColectiva(), Fechas.pasarDateAFormatoSQL(r.getFechaCreacion()));
		System.out.println(sql);

		conn.setAutoCommit(false);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		//actualiza la tabla nueva

		String fechaInicio = "" + r.getFechaInicio().getDate() + "/" + (r.getFechaInicio().getMonth() + 1)
				+ "/" + (r.getFechaInicio().getYear() - 100);

		String fechaFin = "" + r.getFechaFin().getDate() + "/" + (r.getFechaFin().getMonth() + 1) + "/"
				+ (r.getFechaFin().getYear() - 100);

		String fechaCreacionOF = "" + r.getFechaCreacionOferta().getDate() + "/"
				+ (r.getFechaCreacionOferta().getMonth() + 1) + "/"
				+ (r.getFechaCreacionOferta().getYear() - 100);

		sql = "select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin \r\n" + 
				"from reservas\r\n" + 
				"where id_al_of = "+r.getAlojamiento()+" and fecha_inicio = '"+fechaInicio+"' and fecha_creacion_of = '"+fechaCreacionOF+"'";

		rs = st.executeQuery(sql);

		rs.next();

		Log rpta = new Log(rs.getString("ID_AL_OF")+","+
				rs.getString("SEMANA_INICIO")+","+
				rs.getString("ANIO_INICIO")+","+
				rs.getString("SEMANA_FIN")+","+
				rs.getString("ANIO_FIN"));

		String act = rpta.getMsg();

		String[] actuales = act.split(",");

		sql = "select * from alojamientos where id = "+ actuales[0];


		rs = st.executeQuery(sql);

		rs.next();

		String operador = rs.getString("ID_OP");

		//si los años son iguales
		if(actuales[2].equals(actuales[4])) {

			//por cada semana desde el inicio hasta el final
			for(int j = Integer.parseInt(actuales[1]); j <= Integer.parseInt(actuales[3]);j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+" and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);


				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[2] + ",1, "+ operador+")";
					st.executeQuery(sql);
				}
			}
		}
		else {

			//todas las semanas hasta que se acabe la actual
			for(int j = Integer.parseInt(actuales[1]); j <= 52; j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[2]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);			

				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[2]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[2] + ",1,"+ operador+")";
					st.executeQuery(sql);
				}
			}

			//para todos los años entre el primero y el ultimo
			for(int j = Integer.parseInt(actuales[2])+1;j<Integer.parseInt(actuales[4]); j++ ) {

				//para todas las semanas del año
				for(int k = 1; k<=52;k++) {

					sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+k+"and anio = " + j+" and id_aloj = " + actuales[0];
					rs = st.executeQuery(sql);			

					if(rs.next()){

						int numReservas = rs.getInt("NUM_RESERV");

						numReservas++;

						sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+k+" and anio = " + j+"and id_aloj = " + actuales[0];

						st.executeQuery(sql);
					}
					else {

						sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + k + "," + j + ",1, "+ operador+")";
						st.executeQuery(sql);

					}
				}
			}

			//para cada semana desde la primera del ultimo año hasta la ultima
			for(int j = 1; j<=Integer.parseInt(actuales[3]); j++) {

				sql = "select * from NUM_RESERV_ALOJ_SEM where semana = "+j+"and anio = " + actuales[4]+" and id_aloj = " + actuales[0];
				rs = st.executeQuery(sql);			

				if(rs.next()){

					int numReservas = rs.getInt("NUM_RESERV");

					numReservas++;

					sql = "update NUM_RESERV_ALOJ_SEM set NUM_RESERV = "+numReservas+ " where semana = "+j+" and anio = " + actuales[4]+"and id_aloj = " + actuales[0];

					st.executeQuery(sql);
				}
				else {

					sql = "insert into NUM_RESERV_ALOJ_SEM (id_aloj, semana, anio, num_reserv, id_op) values (" + actuales[0] + "," + j + "," + actuales[4] + ",1, "+ operador+")";
					st.executeQuery(sql);

				}
			}
		}

		st.close();
	}

	public ArrayList<Log> getUnidadConMayor(String tipoAl, String unidadTiempo) throws SQLException {

		ArrayList<Log> rpta = new ArrayList<Log>();

		if (unidadTiempo.equals(TipoUnidadTiempo.MES)) {

			String sql = String.format(
					"SELECT tFechas.MES, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1)GROUP BY EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA))tFechas LEFT OUTER JOIN(SELECT MES, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 )GROUP BY EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA))LEFT OUTER JOIN %1$s.RESERVAS ON (MES, ANIO) IN ( SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1))INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = 'HABITACION_HOTEL' AND ESTADO <>  'CANCELADA' GROUP BY MES, ANIO) fechasResv ON tFechas.ANIO = fechasResv.ANIO AND tFechas.MES = fechasResv.MES ORDER BY DEMANDA DESC"
					,USUARIO, tipoAl);

			System.out.println(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			Log max = null;

			if (rs.next()) {
				max = new Log("El mes de mayor demanda es el " + rs.getInt("MES") + "/" + rs.getInt("ANIO")
				+ " del aÃ±o " + rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}

			sql = String.format(
					"SELECT tFechas.MES, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1)GROUP BY EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA))tFechas LEFT OUTER JOIN(SELECT MES, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 )GROUP BY EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA))LEFT OUTER JOIN %1$s.RESERVAS ON (MES, ANIO) IN ( SELECT EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1))INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = 'HABITACION_HOTEL' AND ESTADO <>  'CANCELADA' GROUP BY MES, ANIO) fechasResv ON tFechas.ANIO = fechasResv.ANIO AND tFechas.MES = fechasResv.MES ORDER BY DEMANDA ASC"
					,USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log min = null;
			if (rs.next()) {
				min = new Log("El mes de menor demanda es el " + rs.getInt("MES") + "/" + rs.getInt("ANIO")
				+ " del aÃ±o " + rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}
			sql = String.format(
					"SELECT EXTRACT(MONTH FROM FECHA_FIN) AS MES, EXTRACT(YEAR FROM FECHA_FIN) AS ANIO, SUM(PRECIO_RESERVA) AS DINERO FROM RESERVAS INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE AND TIPO = '%2$s' GROUP BY EXTRACT(MONTH FROM FECHA_FIN), EXTRACT(YEAR FROM FECHA_FIN) ORDER BY SUM(PRECIO_RESERVA) DESC",
					USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log maxDinero = null;

			if (rs.next()) {
				maxDinero = new Log("El mes con mas ingresos es el " + rs.getInt("MES") + "/" + rs.getInt("ANIO")
				+ " con $" + rs.getInt("DINERO"));
			}
			rpta.add(min);
			rpta.add(max);
			rpta.add(maxDinero);
		} else if (unidadTiempo.equals(TipoUnidadTiempo.ANIO)) {

			String sql = String.format(
					"SELECT tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM ( SELECT EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(YEAR FROM FECHA) )tFechas LEFT OUTER JOIN ( SELECT ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(YEAR FROM FECHA) ) LEFT OUTER JOIN %1$s.RESERVAS ON ANIO IN ( SELECT EXTRACT(YEAR FROM FECHA) AS ANIO FROM     (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1 ) ) INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY ANIO )  fechasResv ON tfechas.anio = fechasResv.anio ORDER BY DEMANDA DESC", 
					USUARIO, tipoAl);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			Log max = null;
			if (rs.next()) {
				max = new Log("El aÃ±o de mayor demanda es: " + rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}

			sql = String.format(
					"SELECT tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM ( SELECT EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(YEAR FROM FECHA) )tFechas LEFT OUTER JOIN ( SELECT ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(YEAR FROM FECHA) ) LEFT OUTER JOIN %1$s.RESERVAS ON ANIO IN ( SELECT EXTRACT(YEAR FROM FECHA) AS ANIO FROM     (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1 ) ) INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY ANIO )  fechasResv ON tfechas.anio = fechasResv.anio ORDER BY DEMANDA ASC" 
					,USUARIO, tipoAl);



			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log min = null;
			if (rs.next()) {
				min = new Log("El aÃ±o de menor demanda es: " + rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}

			sql = String.format(
					"SELECT EXTRACT(YEAR FROM FECHA_FIN) AS ANIO, SUM(PRECIO_RESERVA) AS DINERO FROM RESERVAS INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE AND TIPO = 'HABITACION_HOTEL' GROUP BY EXTRACT(YEAR FROM FECHA_FIN) ORDER BY SUM(PRECIO_RESERVA) DESC"
					,USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log maxDinero = null;

			if (rs.next()) {
				maxDinero = new Log("El aÃ±o con mÃ¡s ingresos es " + rs.getInt("ANIO") + " con $" + rs.getInt("DINERO"));
			}
			rpta.add(min);
			rpta.add(max);
			rpta.add(maxDinero);
		} else if (unidadTiempo.equals(TipoUnidadTiempo.SEMANA)) {

			String sql = String.format(
					"SELECT tFechas.SEMANA, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY TO_NUMBER(TO_CHAR(FECHA,'WW')), EXTRACT(YEAR FROM FECHA))tFechas LEFT OUTER JOIN (SELECT SEMANA, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY TO_NUMBER(TO_CHAR(FECHA,'WW')), EXTRACT(YEAR FROM FECHA) ) LEFT OUTER JOIN %1$s.RESERVAS ON (SEMANA, ANIO) IN (  SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1 ) ) INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY SEMANA, ANIO ) fechasResv ON fechasResv.SEMANA = tFechas.SEMANA AND fechasResv.ANIO = tFechas.ANIO ORDER BY DEMANDA DESC"
					, USUARIO, tipoAl);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			Log max = null;
			if (rs.next()) {

				max = new Log("La semana de mayor demanda es la semana numero " + rs.getInt("SEMANA") + " del aÃ±o "
						+ rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");

			}

			sql = String.format(
					"SELECT tFechas.SEMANA, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY TO_NUMBER(TO_CHAR(FECHA,'WW')), EXTRACT(YEAR FROM FECHA))tFechas LEFT OUTER JOIN (SELECT SEMANA, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM %1$s.RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY TO_NUMBER(TO_CHAR(FECHA,'WW')), EXTRACT(YEAR FROM FECHA) ) LEFT OUTER JOIN %1$s.RESERVAS ON (SEMANA, ANIO) IN (  SELECT TO_NUMBER(TO_CHAR(FECHA,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1 ) ) INNER JOIN %1$s.ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY SEMANA, ANIO ) fechasResv ON fechasResv.SEMANA = tFechas.SEMANA AND fechasResv.ANIO = tFechas.ANIO ORDER BY DEMANDA ASC"
					, USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log min = null;
			if (rs.next()) {
				min = new Log("La semana de menor demanda es la semana numero " + rs.getInt("SEMANA") + " del aÃ±o "
						+ rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}
			sql = String.format(
					"SELECT TO_NUMBER(TO_CHAR(FECHA_FIN,'WW'))AS SEMANA, EXTRACT(YEAR FROM FECHA_FIN) AS ANIO, SUM(PRECIO_RESERVA) AS DINERO FROM RESERVAS INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE AND TIPO = '%2$s' GROUP BY TO_NUMBER(TO_CHAR(FECHA_FIN,'WW')), EXTRACT(YEAR FROM FECHA_FIN) ORDER BY SUM(PRECIO_RESERVA) DESC",
					USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log maxDinero = null;

			if (rs.next()) {
				maxDinero = new Log("La semana con mas ingresos es la semana numero " + rs.getInt("SEMANA")
				+ " del aÃ±o " + rs.getInt("ANIO") + " con $" + rs.getInt("DINERO"));
			}

			rpta.add(min);
			rpta.add(max);
			rpta.add(maxDinero);

		}
		else if(unidadTiempo.equals(TipoUnidadTiempo.DIA)) {

			String sql = String.format(
					"SELECT tFechas.DIA, tFechas.MES, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT EXTRACT(DAY FROM FECHA) AS DIA, EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA) )tFechas LEFT OUTER JOIN( SELECT DIA, MES, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(DAY FROM FECHA) AS DIA, EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA) )LEFT OUTER JOIN RESERVAS ON (DIA, MES, ANIO) IN ( SELECT EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1) ) INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY DIA, MES, ANIO) fechasResv ON tFechas.ANIO = fechasResv.ANIO AND tFechas.MES = fechasResv.MES AND tFechas.DIA = fechasResv.DIA ORDER BY DEMANDA DESC"
					,USUARIO, tipoAl);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);

			Log max = null;
			if (rs.next()) {
				max = new Log("El dÃ­a de mayor demanda es: " + rs.getInt("DIA")+ "/" + rs.getInt("MES") +"/"+rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}

			sql = String.format(
					"SELECT tFechas.DIA, tFechas.MES, tFechas.ANIO, (CASE WHEN DEMANDA IS NULL THEN 0 ELSE DEMANDA END) AS DEMANDA FROM (SELECT EXTRACT(DAY FROM FECHA) AS DIA, EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA) )tFechas LEFT OUTER JOIN( SELECT DIA, MES, ANIO, COUNT(*) AS DEMANDA FROM    (   SELECT EXTRACT(DAY FROM FECHA) AS DIA, EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO, COUNT(*) FROM (  SELECT (SELECT MIN(FECHA_INICIO) FROM RESERVAS)  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= CURRENT_DATE - (SELECT MIN(FECHA_INICIO) FROM RESERVAS)+1 ) GROUP BY EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA), EXTRACT(YEAR FROM FECHA) )LEFT OUTER JOIN RESERVAS ON (DIA, MES, ANIO) IN ( SELECT EXTRACT(DAY FROM FECHA), EXTRACT(MONTH FROM FECHA) AS MES, EXTRACT(YEAR FROM FECHA) AS ANIO FROM (  SELECT FECHA_INICIO  + ROWNUM -1 AS FECHA FROM all_objects WHERE ROWNUM <= FECHA_FIN - (FECHA_INICIO)+1) ) INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE TIPO = '%2$s' AND ESTADO <>  'CANCELADA' GROUP BY DIA, MES, ANIO) fechasResv ON tFechas.ANIO = fechasResv.ANIO AND tFechas.MES = fechasResv.MES AND tFechas.DIA = fechasResv.DIA ORDER BY DEMANDA ASC"
					,USUARIO, tipoAl);



			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log min = null;
			if (rs.next()) {
				min = new Log("El dÃ­a de menor demanda es: " + rs.getInt("DIA")+ "/" + rs.getInt("MES") +"/"+rs.getInt("ANIO") + " con " + rs.getInt("DEMANDA") + " reservas registradas");
			}

			sql = String.format(
					"SELECT EXTRACT(YEAR FROM FECHA_FIN) AS ANIO, SUM(PRECIO_RESERVA) AS DINERO FROM RESERVAS INNER JOIN ALOJAMIENTOS ON ID_AL_OF = ID WHERE ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE AND TIPO = 'HABITACION_HOTEL' GROUP BY EXTRACT(YEAR FROM FECHA_FIN) ORDER BY SUM(PRECIO_RESERVA) DESC"
					,USUARIO, tipoAl);

			st = conn.createStatement();
			rs = st.executeQuery(sql);

			Log maxDinero = null;

			if (rs.next()) {
				maxDinero = new Log("El aÃ±o con mÃ¡s ingresos es " + rs.getInt("ANIO") + " con $" + rs.getInt("DINERO"));
			}
			rpta.add(min);
			rpta.add(max);
			rpta.add(maxDinero);

		}

		return rpta;
	}
}
