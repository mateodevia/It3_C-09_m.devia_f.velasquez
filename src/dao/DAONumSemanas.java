package dao;

import java.math.BigInteger;
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
import vos.Alojamiento;
import vos.OfertaAlojamiento;
import vos.Servicio;

public class DAONumSemanas {

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
	
	public DAONumSemanas() {
		recursos = new ArrayList<Object>();
	}
	
	//----------------------------------------------------------------------------------------------
	// Metodos de Comunicacion con la BD
	//----------------------------------------------------------------------------------------------
	
	/**
	 * Metodo que obtiene la informacion de todos los alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void poblarTabla() throws SQLException, Exception {
		
		ArrayList<Log> rptas = new ArrayList<Log>();

		String sql = String.format("select id_al_of, (select to_char(to_date(reservas.fecha_Inicio), 'ww') from dual) as semana_inicio, (extract(year from fecha_inicio)) as anio_inicio, (select to_char(to_date(reservas.fecha_fin), 'ww') from dual) as semana_fin, (extract(year from fecha_fin)) as anio_fin from reservas order by id_al_of");

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );

		while (rs.next()) {
			rptas.add(new Log(rs.getString("ID_AL_OF")+","+
								rs.getString("SEMANA_INICIO")+","+
								rs.getString("ANIO_INICIO")+","+
								rs.getString("SEMANA_FIN")+","+
								rs.getString("ANIO_FIN")));
		}
		
		for(int i = 0; i < rptas.size();i++) {
			
			
			System.out.println(i);
			
			String act = rptas.get(i).getMsg();
			
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
		}
		
		st.close();
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
	public Alojamiento findAlojamientoById(Long id) throws SQLException, Exception 
	{
		Alojamiento alojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS WHERE ID = %2$d", USUARIO, id);
		
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			alojamiento = convertResultSetToAlojamiento(rs);
		}

		st.close();
		return alojamiento;
	}
	/**
	 * Metodo que agregar la informacion de un nuevo alojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		int bool = 0;
		
		if (alojamiento.getCompartida()) {
			
			bool = 1;
		}
		
		String sql = String.format("INSERT INTO %1$s.ALOJAMIENTOS (ID, CAPACIDAD, COMPARTIDA, TIPO, UBICACION, ID_OP) VALUES (%2$s, %3$s, %4$s, '%5$s', '%6$s', %7$s)", 
									USUARIO, 
									alojamiento.getId(),
									alojamiento.getCapacidad(),
									bool,
									alojamiento.getTipoAlojamiento(),
									alojamiento.getUbicacion(),
									alojamiento.getOperador());
		System.out.println(sql);

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();

	}
	
	/**
	 * Metodo que actualiza la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append(String.format("UPDATE %s.ALOJAMIENTOS SET ", USUARIO));
		sql.append(String.format("ID = '%1$s' AND CAPACIDAD = '%2$s' AND COMPARTIDA = '%3$s' AND TIPO = '%4$s'AND UBICACION = '%5$s'",
					alojamiento.getId(), 
					alojamiento.getCapacidad(), 
					alojamiento.getCompartida(), 
					alojamiento.getTipoAlojamiento(), 
					alojamiento.getUbicacion()));
		
		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql.toString() );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	/**
	 * Metodo que borra la informacion del alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteAlojamiento(Alojamiento alojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.ALOJAMIENTOS WHERE ID = %2$d", USUARIO, alojamiento.getId());

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		st.close();
	}
	
	public List<Alojamiento> getAlojamientosBajaDemanda() throws SQLException, Exception {

		String sql = String.format("SELECT %1$s.ALOJAMIENTOS.*, DIAS_MAX_SIN_RESV FROM %1$s.ALOJAMIENTOS LEFT JOIN (SELECT ID_AL_OF, MAX( ROUND(CASE WHEN PROX_INICIO IS NOT NULL THEN FECHA_INICIO ELSE CURRENT_DATE END - FECHA_FIN,0)) AS DIAS_MAX_SIN_RESV FROM ( SELECT %1$s.RESERVAS.*, LEAD(FECHA_INICIO,1) OVER (PARTITION BY ID_AL_OF ORDER BY FECHA_INICIO) AS PROX_INICIO FROM %1$s.RESERVAS WHERE %1$s.RESERVAS.ESTADO <> 'CANCELADA' AND FECHA_FIN < CURRENT_DATE ) GROUP BY ID_AL_OF) ON %1$s.ALOJAMIENTOS.ID = ID_AL_OF WHERE DIAS_MAX_SIN_RESV > 30 OR DIAS_MAX_SIN_RESV IS NULL",
				USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		prepStmt.executeQuery();
		
		List<Alojamiento> resp = new ArrayList<Alojamiento>();
		while(rs.next()) {
			resp.add(convertResultSetToAlojamiento(rs));
		}
		
		st.close();
		return resp;
	}
	
	/**
	 * Metodo que obtiene la informacion de todos los alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Servicio> getServiciosOfAlojamiento( long id ) throws SQLException, Exception {
		ArrayList<Servicio> servicios = new ArrayList<Servicio>();

		String sql = String.format("SELECT * FROM %1$s.ALOJ_SERV WHERE ID_ALOJ= %2$S", USUARIO, id);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		
//		PreparedStatement prepStmt = conn.prepareStatement(sql);
//		recursos.add(prepStmt);
//		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			DAOServicio dao = new DAOServicio();
			dao.setConn(conn);
			Servicio servicio = dao.findServicioById(rs.getLong("ID_SERV"));
			
			servicios.add(servicio);
		}
		
		st.close();
		return servicios;
	}
	
	public long generateNewId() throws SQLException {
		
		String sql = String.format("SELECT MAX(ID) FROM %1$s.ALOJAMIENTOS", USUARIO);

		System.out.println(sql);
		
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );
		rs.next();
		
		long idMax = rs.getLong("MAX(ID)");
		
		
		st.close();
		return idMax+1;
	}

	public List<Alojamiento> getAlojamientosEntreFechasConServicios( Date fechaInicio, Date fechFin, List<Servicio> servicios ) throws Exception {

		ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();

		String sql = "";
		
		if(!servicios.isEmpty()) {

			sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOS WHERE EXISTS (SELECT * FROM (SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s' OR FECHA_INICIO = '%2$s'OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL))DISPONIBLES INNER JOIN %1$s.ALOJ_SERV ON DISPONIBLES.ID_AL = %1$s.ALOJ_SERV.ID_ALOJ WHERE (ID_AL= %1$s.ALOJAMIENTOS.ID AND ID_SERV = %4$s))",
					USUARIO, 
					Fechas.pasarDateAFormatoSQL(fechaInicio), 
					Fechas.pasarDateAFormatoSQL(fechFin),
					servicios.get(0).getId());

			System.out.println("SERVICIOS.size " + servicios.size());
			if (servicios.size() > 1) {

				for(int i = 1; i < servicios.size(); i++) {

					String adicion = String.format("AND EXISTS (SELECT * FROM (SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s' OR FECHA_INICIO = '%2$s'OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL))DISPONIBLES INNER JOIN %1$s.ALOJ_SERV ON DISPONIBLES.ID_AL = %1$s.ALOJ_SERV.ID_ALOJ WHERE (ID_AL= %1$s.ALOJAMIENTOS.ID AND ID_SERV = %4$s))",
							USUARIO, 
							Fechas.pasarDateAFormatoSQL(fechaInicio), 
							Fechas.pasarDateAFormatoSQL(fechFin),
							servicios.get(i).getId());

					sql = sql + adicion;
				}
			}
		}
		else {
			
			sql = String.format("SELECT * FROM %1$s.OFERTAS_ALOJAMIENTOS WHERE FECHA_CREACION < '%2$s' AND  ('%3$s' < FECHA_RETIRO OR FECHA_RETIRO IS NULL) AND NOT EXISTS(SELECT * FROM RESERVAS WHERE (FECHA_INICIO > '%2$s' OR FECHA_FIN < '%3$s'OR FECHA_INICIO = '%2$s' OR FECHA_FIN = '%3$s') AND ID_AL_OF = OFERTAS_ALOJAMIENTOS.ID_AL)",
					USUARIO, 
					Fechas.pasarDateAFormatoSQL(fechaInicio), 
					Fechas.pasarDateAFormatoSQL(fechFin));
		}

		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery( sql );

		while (rs.next()) {
			
			alojamientos.add(convertResultSetToAlojamiento(rs));
		}
		
		return alojamientos;
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
	public Alojamiento convertResultSetToAlojamiento(ResultSet resultSet) throws Exception {
		
		long id = resultSet.getLong("ID");
		int capacidad = resultSet.getInt("CAPACIDAD");
		boolean compartida = resultSet.getBoolean("COMPARTIDA");
		String tipo = resultSet.getString("TIPO");
		String ubicacion = resultSet.getString("UBICACION");
		long idOp = resultSet.getLong("ID_OP");
		ArrayList<Servicio> servicios = getServiciosOfAlojamiento(id);
		
		DAOOfertaAlojamiento daoOferta = new DAOOfertaAlojamiento();
		daoOferta.setConn(conn);
		List<OfertaAlojamiento> ofertas = daoOferta.getOfertasOfAlojamiento(id);

		Alojamiento alojamiento = new Alojamiento(id, capacidad, compartida, tipo, ubicacion, servicios, ofertas, idOp);

		return alojamiento;
	}
	
	
	
}
