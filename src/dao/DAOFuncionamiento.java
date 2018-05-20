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

		public ArrayList<RFC12> getFuncionamiento(Integer anio) throws SQLException {

			ArrayList<RFC12> respuesta = new ArrayList<RFC12>();

			for(int i = 1; i<53; i++) {

				respuesta.add(new RFC12(i));	
			}

			String sqlAloj = "select numero as semana, \r\n" + 
					"        (select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento\r\n" + 
					"        from (select id_aloj, num_reserv\r\n" + 
					"                from(select id_aloj, num_reserv \r\n" + 
					"                        from NUM_RESERV_ALOJ_SEM \r\n" + 
					"                        where semana = numero and anio = "+anio+" \r\n" + 
					"                        order by num_reserv desc\r\n" + 
					"                    ) uno\r\n" + 
					"                where rownum = 1\r\n" + 
					"            ) dos\r\n" + 
					"        inner join\r\n" + 
					"        alojamientos\r\n" + 
					"        on dos.id_aloj = alojamientos.id\r\n" + 
					"        ) as alojamiento_mayor_ocupacion,\r\n" + 
					"        (select ('id: '||id_aloj||' capacidad: '||capacidad||' compartida: '||compartida||' tipo: '||tipo||' ubicacion: '||ubicacion||' operador: '||id_op||' numero de reservas: '||num_reserv) as alojamiento\r\n" + 
					"        from (select id_aloj, num_reserv\r\n" + 
					"                from(select id_aloj, num_reserv \r\n" + 
					"                        from NUM_RESERV_ALOJ_SEM \r\n" + 
					"                        where semana = numero and anio = "+anio+" \r\n" + 
					"                        order by num_reserv asc\r\n" + 
					"                    ) uno\r\n" + 
					"                where rownum = 1\r\n" + 
					"            ) dos\r\n" + 
					"        inner join\r\n" + 
					"        alojamientos\r\n" + 
					"        on dos.id_aloj = alojamientos.id\r\n" + 
					"        ) as alojamiento_menor_ocupacion,\r\n" + 
					"        (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento\r\n" + 
					"        from (select *\r\n" + 
					"                from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = "+anio+" group by ID_OP order by num_reservas desc) seis\r\n" + 
					"                where rownum = 1\r\n" + 
					"            )siete\r\n" + 
					"            inner join\r\n" + 
					"            operadores\r\n" + 
					"            on siete.id_op = operadores.id\r\n" + 
					"        )as operador_mas_solicitado,\r\n" + 
					"\r\n" + 
					"        (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento\r\n" + 
					"        from (select *\r\n" + 
					"                from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = "+anio+" group by ID_OP order by num_reservas asc) seis\r\n" + 
					"                where rownum = 1\r\n" + 
					"            )siete\r\n" + 
					"            inner join\r\n" + 
					"            operadores\r\n" + 
					"            on siete.id_op = operadores.id\r\n" + 
					"        )as operador_menos_solicitado\r\n" + 
					"from semanas";

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlAloj);

			RFC12 rf;
			int semana;
			while(rs.next()) {
				semana = rs.getInt("SEMANA");
				rf = respuesta.get(semana-1);
				rf.setAlojamientoMasOcupacion(rs.getString("ALOJAMIENTO_MAYOR_OCUPACION"));
				rf.setAlojamientoMenosOcupacion(rs.getString("ALOJAMIENTO_MENOR_OCUPACION"));
				rf.setOperadorMasSolicitado(rs.getString("OPERADOR_MAS_SOLICITADO"));
				rf.setOperadorMenosSolicitado(rs.getString("OPERADOR_MENOS_SOLICITADO"));
			}

//			String sqlOperadores = "select numero as semana, (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento\r\n" + 
//					"                            from (select *\r\n" + 
//					"                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = "+anio+" group by ID_OP order by num_reservas desc) seis\r\n" + 
//					"                                    where rownum = 1\r\n" + 
//					"                                )siete\r\n" + 
//					"                                inner join\r\n" + 
//					"                                operadores\r\n" + 
//					"                                on siete.id_op = operadores.id\r\n" + 
//					"                        )as operador_mas_solicitado,\r\n" + 
//					"\r\n" + 
//					"                        (select ('id: '||id_op||' nombre: '||nombre||' tipo: '||tipo||' numero de reservas: '||num_reservas) as alojamiento\r\n" + 
//					"                            from (select *\r\n" + 
//					"                                    from (select id_op, sum(num_reserv) num_reservas from NUM_RESERV_ALOJ_SEM where semana = numero and anio = "+anio+" group by ID_OP order by num_reservas asc) seis\r\n" + 
//					"                                    where rownum = 1\r\n" + 
//					"                                )siete\r\n" + 
//					"                                inner join\r\n" + 
//					"                                operadores\r\n" + 
//					"                                on siete.id_op = operadores.id\r\n" + 
//					"                        )as operador_menos_solicitado\r\n" + 
//					"from semanas";
//
//			rs = st.executeQuery(sqlOperadores);
//
//			while(rs.next()) {
//				semana = rs.getInt("SEMANA");
//				rf = respuesta.get(semana-1);
//				rf.setOperadorMasSolicitado(rs.getString("OPERADOR_MAS_SOLICITADO"));
//				rf.setOperadorMenosSolicitado(rs.getString("OPERADOR_MENOS_SOLICITADO"));
//
//			}
//			
			return respuesta;
		}
}
