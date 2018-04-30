package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import vos.Alojamiento;
import vos.HabHost;
import vos.HabHotel;
import vos.Hotel;
import vos.PersonaNatural;

public class DAOHabHotel {
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
		
		public DAOHabHotel() {
			recursos = new ArrayList<Object>();
		}
		
		//----------------------------------------------------------------------------------------------
		// Metodos de Comunicacion con la BD
		//----------------------------------------------------------------------------------------------
		
		/**
		 * Metodo que obtiene la informacion de todos las habHotel en la Base de Datos <br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
		 * @return	lista con la informacion de todas las habHotel que se encuentran en la Base de Datos
		 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public ArrayList<HabHotel> getHabHotel() throws SQLException, Exception {
			
			ArrayList<HabHotel> habHoteles = new ArrayList<HabHotel>();

			String sql = String.format("SELECT * FROM %1$s.HABS_HOTELES", USUARIO);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			recursos.add(prepStmt);
//			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				habHoteles.add(convertResultSetToHabHotel(rs));
			}
			
			st.close();
			return habHoteles;
		}
		
		/**
		 * Metodo que obtiene la informacion de la habHotel en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
		 * @param id el identificador de la habHotel
		 * @return la informacion de la habHotel que cumple con los criterios de la sentecia SQL
		 * 			Null si no existe la habHotel con los criterios establecidos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public HabHotel findHabHotelById(Long id) throws SQLException, Exception 
		{
			HabHotel habHotel = null;

			String sql = String.format("SELECT * FROM %1$s.HABS_HOTELES WHERE ID_AL = %2$d", USUARIO, id); 

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			recursos.add(prepStmt);
//			ResultSet rs = prepStmt.executeQuery();

			if(rs.next()) {
				habHotel = convertResultSetToHabHotel(rs);
			}

			st.close();
			return habHotel;
		}
		/**
		 * Metodo que agregar la informacion de una nueva habHotel en la Base de Datos a partir del parametro ingresado<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param habHotel HabHotel que desea agregar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void addHabHotel(HabHotel habHotel) throws SQLException, Exception {

			String sql = String.format("INSERT INTO %1$s.HABS_HOTELES (ID_AL, NUM_HAB, TIPO, TAMAÑO) VALUES (%2$s, '%3$s', '%4$s', '%5$s')", 
										USUARIO, 
										habHotel.getId(),
										habHotel.getNumero(),
										habHotel.getTipo(),
										habHotel.getTamanio());
			System.out.println(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			recursos.add(prepStmt);
//			prepStmt.executeQuery();
			
			st.close();

		}
		
		/**
		 * Metodo que actualiza la informacion de la habHotel en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param habHotel HabHotel que desea actualizar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void updateHabHotel(HabHotel habHotel) throws SQLException, Exception {

			StringBuilder sql = new StringBuilder();
			sql.append(String.format("UPDATE %s.HABS_HOTELES SET ", USUARIO));
			sql.append(String.format("ID_AL	 = '%1$s' AND NUM_HAB = '%2$s' AND TIPO = '%3$s' AND TAMAÑO = '%4$s' AND ID_OP = '%5$s' ",
					habHotel.getId(),
					habHotel.getNumero(),
					habHotel.getTipo(),
					habHotel.getTamanio()));
			
			System.out.println(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql.toString() );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
//			recursos.add(prepStmt);
//			prepStmt.executeQuery();
			st.close();
		}
		
		/**
		 * Metodo que borra la informacion de la habHotel en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param habHotel HabHotel que desea actualizar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void deleteHabHotel(HabHotel habHotel) throws SQLException, Exception {

			String sql = String.format("DELETE FROM %1$s.HABS_HOTELES WHERE ID_AL = %2$d", USUARIO, habHotel.getId());

			System.out.println(sql);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			recursos.add(prepStmt);
//			prepStmt.executeQuery();
			
			st.close();
		}
		
		/**
		 * Metodo que obtiene la informacion de todos las habHotel de un hotel <br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
		 * @return	lista con la informacion de todas las habitaciones de un hotel
		 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public ArrayList<HabHotel> getHabHotelOfHotel( long id ) throws SQLException, Exception {
			
			ArrayList<HabHotel> habHotel = new ArrayList<HabHotel>();

			String sql = String.format("SELECT * FROM %1$s.HABS_HOSTS INNER JOIN %1$s.ALOJAMIENTOS ON ID = ID_AL WHERE ID_OP = %2$s", USUARIO, id);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery( sql );
			
//			PreparedStatement prepStmt = conn.prepareStatement(sql);
//			recursos.add(prepStmt);
//			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				
				habHotel.add(convertResultSetToHabHotel(rs));
			}
			
			st.close();
			return habHotel;
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
		public HabHotel convertResultSetToHabHotel(ResultSet resultSet) throws Exception {
			
			long id = resultSet.getLong("ID_AL");
			int numHabitacion = resultSet.getInt("NUM_HAB");
			String tipo = resultSet.getString("TIPO");
			double tamanio = resultSet.getDouble("TAMAÑO");
			long idOp = resultSet.getLong("ID_OP");
			
			DAOAlojamiento daoAlojamiento = new DAOAlojamiento();
			daoAlojamiento.setConn(conn);
			Alojamiento aloja = daoAlojamiento.findAlojamientoById(id);
					
			HabHotel habHotel = new HabHotel(id, aloja.getCapacidad(), aloja.getCompartida(), aloja.getTipoAlojamiento(), aloja.getUbicacion(), aloja.getServicios(), aloja.getOfertas(), numHabitacion, tipo, tamanio, idOp);

			return habHotel;
		}
}
