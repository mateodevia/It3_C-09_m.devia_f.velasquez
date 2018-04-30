package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import auxiliary.Fechas;
import auxiliary.RFC3;
import vos.Alojamiento;
import vos.OfertaAlojamiento;
import vos.Operador;
import vos.Reserva;
import vos.Servicio;

public class DAOOfertaAlojamiento {
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

	public DAOOfertaAlojamiento() {
		recursos = new ArrayList<Object>();
	}

	// ----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	// ----------------------------------------------------------------------------------------------
	/**
	 * Metodo que obtiene la informacion de todos las ofertaAlojamiento en la Base
	 * de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @return lista con la informacion de todos las ofertaAlojamiento que se
	 *         encuentran en la Base de Datos
	 * @throws SQLException
	 *             Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<OfertaAlojamiento> getOfertasAlojamiento() throws SQLException, Exception {
		ArrayList<OfertaAlojamiento> ofertasAlojamiento = new ArrayList<OfertaAlojamiento>();

		String sql = String.format("SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS", USUARIO);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ofertasAlojamiento.add(convertResultSetToOfertaAlojamiento(rs));
		}

		rs.close();
		st.close();
		return ofertasAlojamiento;
	}

	/**
	 * Metodo que obtiene la informacion del ofertAlojamiento en la Base de Datos
	 * que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param id
	 *            el identificador de la ofertaAlojamiento
	 * @return la informacion de la ofertaAlojamiento que cumple con los criterios
	 *         de la sentecia SQL Null si no existe la ofertaAlojamiento con los
	 *         criterios establecidos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public OfertaAlojamiento findOfertaAlojamientoByPK(Long id_al, Date fechaCreacion) throws SQLException, Exception {

		OfertaAlojamiento ofertaAlojamiento = null;

		String sql = String.format(
				"SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE ID_AL = %2$d AND FECHA_CREACION = '" + Fechas.pasarDateAFormatoSQL(fechaCreacion) + "'",
				USUARIO, id_al);
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			ofertaAlojamiento = convertResultSetToOfertaAlojamiento(rs);
		}
		
		return ofertaAlojamiento;
	}
	
	

	/**
	 * Metodo que agregar la informacion de ua nueva ofertaAlojamiento en la Base de
	 * Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param ofertaAlojamiento
	 *            OfertaAlojamiento que desea agregar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void addOfertaAlojamiento(OfertaAlojamiento ofertaAlojamiento) throws SQLException, Exception {

		String sql = String.format(
				"INSERT INTO %1$s.OFERTAS_ALOJAMIENTOS (PRECIO, UNIDAD_PRECIO, ID_AL, FECHA_CREACION) VALUES (%2$s, '%3$s', %4$s, CURRENT_DATE)",
				USUARIO, ofertaAlojamiento.getPrecio(), ofertaAlojamiento.getUnidadDePrecio(),
				ofertaAlojamiento.getAlojamiento());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		rs.close();
		st.close();

	}
	
	public void addOfertaAlojamientoD(OfertaAlojamiento ofertaAlojamiento) throws SQLException, Exception {

		String sql = String.format(
				"INSERT INTO %1$s.OFERTAS_ALOJAMIENTOS (PRECIO, UNIDAD_PRECIO, ID_AL, FECHA_CREACION) VALUES (%2$s, '%3$s', %4$s, '%5$s')",
				USUARIO, ofertaAlojamiento.getPrecio(), ofertaAlojamiento.getUnidadDePrecio(),
				ofertaAlojamiento.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(ofertaAlojamiento.getFechaCreacion()));
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		rs.close();
		st.close();

	}

	/**
	 * Metodo que actualiza la informacion de la ofertaAlojamiento en la Base de
	 * Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param ofertaAlojamiento
	 *            OfertaAlojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	@SuppressWarnings("deprecation")
	public void updateOfertaAlojamiento(OfertaAlojamiento ofertaAlojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();

		String fecha = null;
		String dia;
		String mes;
		if (ofertaAlojamiento.getFechaRetiro() != null) {

			dia = ((ofertaAlojamiento.getFechaRetiro().getDate() < 10)
					? "0" + ofertaAlojamiento.getFechaRetiro().getDate()
					: "" + ofertaAlojamiento.getFechaRetiro().getDate());
			mes = ((ofertaAlojamiento.getFechaRetiro().getMonth() + 1 < 10)
					? "0" + (ofertaAlojamiento.getFechaRetiro().getMonth() + 1)
					: "" + ofertaAlojamiento.getFechaRetiro().getMonth() + 1);
			fecha = dia + "/" + mes + "/" + (ofertaAlojamiento.getFechaRetiro().getYear() - 100);
		}

		String fechaCreacion = null;
		if (ofertaAlojamiento.getFechaCreacion() != null) {
			fechaCreacion = "" + ofertaAlojamiento.getFechaCreacion().getDate() + "/"
					+ (ofertaAlojamiento.getFechaCreacion().getMonth() + 1) + "/"
					+ (ofertaAlojamiento.getFechaCreacion().getYear() - 100);
		}

		sql.append(String.format("UPDATE %s.OFERTAS_ALOJAMIENTOS SET ", USUARIO));
		sql.append(String.format(
				"PRECIO = %1$s, FECHA_RETIRO = '%2$s', UNIDAD_PRECIO = '%3$s', ID_AL = %4$s, FECHA_CREACION = '%5$s'",
				ofertaAlojamiento.getPrecio(), 
				fecha, 
				ofertaAlojamiento.getUnidadDePrecio(),
				ofertaAlojamiento.getAlojamiento(), 
				Fechas.pasarDateAFormatoSQL(ofertaAlojamiento.getFechaCreacion())));
		
		sql.append(String.format(" WHERE ID_AL = %1$s AND FECHA_CREACION = '%2$s'", ofertaAlojamiento.getAlojamiento(),
				Fechas.pasarDateAFormatoSQL(ofertaAlojamiento.getFechaCreacion())));
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql.toString());

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		rs.close();
		st.close();

	}

	/**
	 * Metodo que borra la informacion de la ofertaAlojamiento en la Base de Datos
	 * que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param ofertaAlojamiento
	 *            OfertaAlojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void deleteOfertaAlojamiento(OfertaAlojamiento ofertaAlojamiento) throws SQLException, Exception {

		String sql = String.format(
				"DELETE FROM %1$s.OFERTAS_ALOJAMIENTO WHERE ID_AL = %2$d AND FECHA_CREACION = '%3$s'", USUARIO,
				ofertaAlojamiento.getAlojamiento(), ofertaAlojamiento.getFechaCreacion());

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		rs.close();
		st.close();

	}

	/**
	 * Metodo que obtiene la informacion de la oferta perteneciente al alojamiento
	 * con id entrado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param id
	 *            el identificador del alojamiento
	 * @return la informacion de la oferta del alojamiento que cumple con los
	 *         criterios de la sentecia SQL Null si no existe el alojamiento con los
	 *         criterios establecidos
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public List<OfertaAlojamiento> getOfertasOfAlojamiento(long idAl) throws Exception {

		List<OfertaAlojamiento> ofertasAlojamiento = new ArrayList<OfertaAlojamiento>();

		String sql = String.format("SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE ID_AL = %2$d", USUARIO, idAl);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		if (rs.next()) {
			ofertasAlojamiento.add(convertResultSetToOfertaAlojamiento(rs));
		}

		rs.close();
		st.close();
		return ofertasAlojamiento;
	}

	/**
	 * Método que retorna las ofertas de alojamientos ordenadas por popularidad
	 * (número de reservas correspondientes)
	 * 
	 * @return todas las reservas, ordenadas por popularidad
	 */
	public List<OfertaAlojamiento> getOfertasMasPopulares() throws Exception {

		String sql = String.format(
				"SELECT OA.ID_AL, OA.FECHA_CREACION, SUM(CASE WHEN (R.ID_AL_OF IS NOT NULL AND R.FECHA_CREACION_OF IS NOT NULL AND R.FECHA_INICIO IS NOT NULL)  THEN 1 ELSE 0 END) AS NUMRESERVAS FROM (%1$s.OFERTAS_ALOJAMIENTOS OA LEFT OUTER JOIN %1$s.RESERVAS R ON OA.ID_AL = R.ID_AL_OF AND OA.FECHA_CREACION = R.FECHA_CREACION_OF)GROUP BY OA.ID_AL, OA.FECHA_CREACION ORDER BY NUMRESERVAS DESC",
				USUARIO);

		// String sql = "SELECT OA.ID, SUM(CASE WHEN R.ID IS NOT NULL THEN 1 ELSE 0 END)
		// AS NUMRESERVAS FROM (" + USUARIO +".OFERTAS_ALOJAMIENTOS OA LEFT OUTER JOIN "
		// +USUARIO +".RESERVAS R ON OA.ID = R.ID_OFERTA) GROUP BY OA.ID ORDER BY
		// NUMRESERVAS DESC";
		
		System.out.println("SENTENCIA: " + sql);

		Statement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery(sql);

		ArrayList<OfertaAlojamiento> list = new ArrayList<OfertaAlojamiento>();
		OfertaAlojamiento oa;

		int cuenta = 0;
		while (rs.next()) {

			Long idOfAl = rs.getLong("ID_AL");
			Date fechaCreacion = rs.getDate("FECHA_CREACION");
			list.add(findOfertaAlojamientoByPK(idOfAl, fechaCreacion));
		}

		rs.close();
		st.close();
		return list;
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
			if (ob instanceof Statement)
				try {
					((Statement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			else if (ob instanceof Statement)
				try {
					((Statement) ob).close();
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
	public OfertaAlojamiento convertResultSetToOfertaAlojamiento(ResultSet resultSet) throws Exception {

		double precio = resultSet.getDouble("PRECIO");
		Date fechaRetiro = resultSet.getDate("FECHA_RETIRO");
		String unidadPrecio = resultSet.getString("UNIDAD_PRECIO");
		long idAl = resultSet.getLong("ID_AL");

		DAOReserva daoReserva = new DAOReserva();
		daoReserva.setConn(conn);
		ArrayList<Reserva> reservas = daoReserva.getReservasOfOfertaAlojamiento(idAl);

		Date fechaCreacion = resultSet.getDate("FECHA_CREACION");

		OfertaAlojamiento ofertaAlojamiento = new OfertaAlojamiento(precio, fechaRetiro, unidadPrecio, idAl, reservas,
				fechaCreacion);

		return ofertaAlojamiento;
	}

	public OfertaAlojamiento getOfertaMayorFechaRetiroOfAlojamiento(long id) throws Exception {

		String sql = String.format(
				"SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE ID_AL = %2$d AND FECHA_RETIRO IS NOT NULL ORDER BY FECHA_RETIRO DESC",
				USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		OfertaAlojamiento o = null;

		if (rs.next()) {
			o = convertResultSetToOfertaAlojamiento(rs);
		}

		rs.close();
		st.close();
		return o;
	}

	public OfertaAlojamiento getOfertaWithOutRetiroOfAlojamiento(long id) throws Exception {

		String sql = String.format(
				"SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE ID_AL = %2$d AND FECHA_RETIRO IS NULL", USUARIO, id);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		// PreparedStatement prepStmt = conn.prepareStatement(sql);
		// recursos.add(prepStmt);
		// ResultSet rs = prepStmt.executeQuery();

		OfertaAlojamiento o = null;

		if (rs.next()) {
			o = convertResultSetToOfertaAlojamiento(rs);
		}

		rs.close();
		st.close();
		return o;
	}

	public List<RFC3> getIndiceOcupacion() throws SQLException {

		ArrayList<RFC3> indices = new ArrayList<RFC3>();

		String sql = String.format(
				"select id, (SELECT to_binary_double(ROUND((DOS.DIAS_OCUPADO)/(UNO.DIAS_OFERTADO),2))AS INDICE FROM (SELECT ROUND( SUM(CASE WHEN FECHA_RETIRO IS NULL THEN (CURRENT_DATE - FECHA_CREACION) ELSE (FECHA_RETIRO - FECHA_CREACION) END), 0) AS DIAS_OFERTADO FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE ID_AL = id)UNO, (SELECT SUM(FECHA_FIN-FECHA_INICIO)DIAS_OCUPADO FROM %1$s.RESERVAS WHERE ID_AL_OF = id)DOS) as indice from alojamientos",
				USUARIO);

		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			indices.add(new RFC3(rs.getLong("ID"), rs.getDouble("INDICE")));
		}

		return indices;
	}

	public String deshabilitarOfertaDeAlojamiento(Long idAlOf, Date fechaAlOf, Date fechaDeshab) throws Exception {

		String s = "Log de deshabilitación de oferta de alojamiento.\n";
		s += "Deshabilitando oferta de alojamiento con PK (idAl,fechaCreacion) = ( " + idAlOf + ", " + fechaAlOf
				+ " )\n";

		conn.setAutoCommit(false);
		Statement st = conn.createStatement();

		// Actua como fifo, definiendo las prioridades de re arreglo de cada reserva.
		ArrayList<Reserva> queue = new ArrayList<Reserva>();

		// Esto significa que se deshabilita desde fechaActual, si una reserva
		// se encuentra ejecutándose, entonces tiene máxima prioridad de cambiar
		// alojamiento
		if (fechaDeshab == null) {
			fechaDeshab = new Date(System.currentTimeMillis());

			String sql = String.format(
					"SELECT * FROM %1$s.RESERVAS WHERE FECHA_INICIO < CURRENT_DATE AND FECHA_FIN>CURRENT_DATE AND ID_AL_OF = %2$s AND FECHA_CREACION_OF = '%3$s' AND ESTADO <> 'CANCELADA'",
					USUARIO, idAlOf, Fechas.pasarDateAFormatoSQL(fechaAlOf));
			ResultSet rs = st.executeQuery(sql);

			if (rs.next()) {
				Reserva r = DAOReserva.convertResultSetToReserva(rs);
				r.setReservaColectiva(null);
				queue.add(r);
			}
		}

		String sql = String.format(
				"SELECT * FROM %1$s.RESERVAS WHERE FECHA_INICIO > '%2$s' AND ID_AL_OF = %3$s AND FECHA_CREACION_OF = '%4$s' AND ESTADO <> 'CANCELADA' ORDER BY FECHA_CREACION",
				USUARIO, Fechas.pasarDateAFormatoSQL(fechaDeshab), idAlOf, Fechas.pasarDateAFormatoSQL(fechaAlOf));

		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Reserva r = DAOReserva.convertResultSetToReserva(rs);
			r.setReservaColectiva(null);
			queue.add(r);
		}
		
		// {Q1: queue contiene todas las reservas que se deben rearreglar.}
		// Ahora: por cada reserva se debe buscar una oferta alternativa. Una oferta
		// alternativa
		// es aquella que:
		// -El alojamiento contiene los mismos servicios que la que se va a cancelar.
		// -Está disponible en las mismas fechas que la oferta original. Es decir,
		// no coincide con otras reservas ni es después de la fecha de retiro, si hay
		// una.
		// -La capacidad del alojamiento es menor o igual al número de personas de la
		// reserva.
		// -status de compartida se mantiene.

		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOReserva daoR = new DAOReserva();
		daoAl.setConn(conn);
		daoR.setConn(conn);
		Alojamiento a = null;

		try {
			// retirar of alojamiento.
			conn.setAutoCommit(false);
			OfertaAlojamiento oa = findOfertaAlojamientoByPK(idAlOf, fechaAlOf);
			oa.setFechaRetiro(fechaDeshab);
			updateOfertaAlojamiento(oa);
			conn.commit();

			for (Reserva r : queue) {
				try {

					a = daoAl.findAlojamientoById(r.getAlojamiento());

					sql = String.format(
							"SELECT ID_AL, %1$s.OFERTAS_ALOJAMIENTOS.FECHA_CREACION FROM %1$s.OFERTAS_ALOJAMIENTOS INNER JOIN %1$s.ALOJAMIENTOS ON %1$s.ALOJAMIENTOS.ID = %1$s.OFERTAS_ALOJAMIENTOS.ID_AL WHERE ID_AL NOT IN( SELECT ID_AL_OF AS ID_AL FROM %1$s.RESERVAS WHERE (NOT('%2$s'>FECHA_FIN OR FECHA_INICIO > '%3$s'))) AND (%1$s.OFERTAS_ALOJAMIENTOS.FECHA_CREACION, %1$s.OFERTAS_ALOJAMIENTOS.ID_AL) IN (SELECT MAX(FECHA_CREACION),ID_AL FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE %1$s.OFERTAS_ALOJAMIENTOS.FECHA_RETIRO>CURRENT_DATE OR FECHA_RETIRO IS NULL GROUP BY ID_AL) AND %4$s <= %1$s.ALOJAMIENTOS.CAPACIDAD AND %5$s = %1$s.ALOJAMIENTOS.COMPARTIDA",
							USUARIO, Fechas.pasarDateAFormatoSQL(r.getFechaInicio()),
							Fechas.pasarDateAFormatoSQL(r.getFechaFin()), r.getNumPersonas(),
							(a.getCompartida()) ? 1 : 0);

					for (Servicio serv : a.getServicios()) {
						String add = String.format(
								" AND '%2$s' IN (SELECT %1$s.SERVICIOS.TIPO FROM %1$s.ALOJ_SERV INNER JOIN %1$s.SERVICIOS ON %1$s.ALOJ_SERV.ID_SERV = %1$s.SERVICIOS.ID WHERE ID_ALOJ = ID_AL )",
								USUARIO, serv.getTipo());
						sql += add;
					}

					System.out.println("QUERY : " + sql);

					rs = st.executeQuery(sql);

					// Si hay next, se puede reacomodar la reserva a ese next. Si no,
					// Se debe registrar que no fue posible reacomodarla.
					if (rs.next()) {
						try {
							daoR.cancelarReservaD(r);
							
							String pk1 = "( " + r.getFechaInicio() + ", " + r.getAlojamiento() + ", "
									+ r.getFechaCreacionOferta() + " )";
							r.setAlojamiento(rs.getLong("ID_AL"));
							r.setFechaCreacionOferta(rs.getDate("FECHA_CREACION"));
							daoR.addReservaD(r);
							conn.commit();
							
							
							s += "fue posible reacomodar a la reserva (FECHA_INICIO, ID_AL_OF, FECHA_CREACION_OF) = "
									+ pk1 + " la cual se ha vuelto en ("
									+ Fechas.pasarDateAFormatoSQL(r.getFechaInicio()) + ", " + r.getAlojamiento() + ", "
									+ r.getFechaCreacionOferta() + " ) \n";
						} catch (Exception e) {
							
							// Si no es posible reacomodarlas, sólo cancelelas.
							s += "no fue posible reacomodar a la reserva (FECHA_INICIO, ID_AL_OF, FECHA_CREACION_OF) = ("
									+ Fechas.pasarDateAFormatoSQL(r.getFechaInicio()) + ", " + r.getAlojamiento() + ", "
									+ r.getFechaCreacionOferta() + " ) " + "RAZON: " + e.getMessage() + "\n";
							e.printStackTrace();
							conn.rollback();

							daoR.cancelarReservaD(r);
							conn.commit();
						}

					} else {
						

						// Si no es posible reacomodarlas, sólo cancelelas.
						s += "no fue posible reacomodar a la reserva (FECHA_INICIO, ID_AL_OF, FECHA_CREACION_OF) = ("
								+ Fechas.pasarDateAFormatoSQL(r.getFechaInicio()) + ", " + r.getAlojamiento() + ", "
								+ r.getFechaCreacionOferta() + " ) " + "RAZON: falta de disponibilidad \n";
						
								daoR.cancelarReservaD(r);
								conn.commit();
					}
				} catch (SQLException e) {
					System.out.println("ERROR INESPERADO " + e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR RETIRANDO EL ALOJAMIENTO " + e);
			e.printStackTrace();
			conn.rollback();
		}

		st.close();
		daoAl.cerrarRecursos();
		daoR.cerrarRecursos();
		return s;
	}

}
