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
import auxiliary.RFC10;
import auxiliary.RFC12;
import auxiliary.RFC13;
import auxiliary.UsoCliente;
import auxiliary.UsoTipoCliente;
import vos.Cliente;
import vos.Reserva;

public class DAOFuncionamiento {

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
		
		public DAOFuncionamiento() {
			recursos = new ArrayList<Object>();
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
		

		//----------------------------------------------------------------------------------------------
		// Metodos de Comunicacion con la BD
		//----------------------------------------------------------------------------------------------
		
		public void getFuncionamiento(Integer anio) {
					
		ArrayList<RFC12> respuesta = new ArrayList<RFC12>();
		
		for(int i = 1; i<53; i++) {
			
			respuesta.add(new RFC12(i));	
		}
		
		String sqlAlojMas = String.format("select numero as semana, (select concat(alojamiento, concat(', numero de reservas: ',reservas))aloj_reserv\r\n" + 
				"from(select concat('id: ',concat(id, concat(', capacidad: ', concat(capacidad,concat(', compartida: ', concat(compartida, concat(', tipo: ', concat(tipo, concat(', ubicacion: ', ubicacion))))))))) alojamiento, (select count(*)\r\n" + 
				"        from (select FECHA_INICIO,\r\n" + 
				"                    fecha_fin,\r\n" + 
				"                    ID_AL_OF, \r\n" + 
				"                    FECHA_CREACION_OF,         \r\n" + 
				"                    case when extract(year from fecha_inicio) = %1$s then to_char(to_date(fecha_inicio), 'ww') else '0' end as semana_inicio, \r\n" + 
				"                    case when extract(year from fecha_fin) = %1$s then to_char(to_date(fecha_fin), 'ww') else '52' end as semana_fin \r\n" + 
				"                from reservas\r\n" + 
				"                where extract(year from fecha_inicio) = %1$s or \r\n" + 
				"                    extract(year from fecha_inicio) < %1$s  and (extract(year from fecha_fin) > %1$s or extract(year from fecha_fin) = %1$s) or \r\n" + 
				"                    extract(year from fecha_fin) = %1$s or \r\n" + 
				"                    extract(year from fecha_fin) > %1$s  and (extract(year from fecha_inicio) < %1$s or extract(year from fecha_inicio) = %1$s)\r\n" + 
				"            )uno\r\n" + 
				"        where (uno.semana_inicio < numero or uno.semana_inicio = numero) and (uno.semana_fin > numero or uno.semana_fin = numero) and UNO.ID_AL_OF = id) reservas\r\n" + 
				"from alojamientos\r\n" + 
				"order by reservas  desc)\r\n" + 
				"where rownum = 1) alojamiento_mayor__ocupacion\r\n" + 
				"from semanas", anio);
		
			
		/*ArrayList<Cliente> clientes = new ArrayList<Cliente>();

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
			*/
		}
}
