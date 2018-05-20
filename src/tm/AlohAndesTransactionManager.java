package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import auxiliary.Log;
import auxiliary.RFC1;
import auxiliary.RFC10;
import auxiliary.RFC12;
import auxiliary.RFC13;
import auxiliary.RFC3;
import auxiliary.UsoCliente;
import auxiliary.UsoTipoCliente;

import java.util.ArrayList;

import dao.DAOAlojamiento;
import dao.DAOApartamento;
import dao.DAOCasa;
import dao.DAOCliente;
import dao.DAOFuncionamiento;
import dao.DAOHabHost;
import dao.DAOHabHostal;
import dao.DAOHabHotel;
import dao.DAOHabVivienda;
import dao.DAOHostal;
import dao.DAOHotel;
import dao.DAONumSemanas;
import dao.DAOOfertaAlojamiento;
import dao.DAOOperador;
import dao.DAOPersonaNatural;
import dao.DAOReserva;
import dao.DAOReservaColectiva;
import dao.DAOServicio;
import dao.DAOViviendaUniversitaria;
import exceptions.BusinessLogicException;
import tipo.TipoAgrupamiento;
import tipo.TipoAlojamiento;
import tipo.TipoEstado;
import tipo.TipoHabitacion;
import tipo.TipoOperador;
import tipo.TipoOrdenamiento;
import tipo.TipoPersona;
import tipo.TipoServicio;
import tipo.TipoUnidadCobro;
import tipo.TipoUnidadTiempo;
import vos.Alojamiento;
import vos.Apartamento;
import vos.Casa;
import vos.Cliente;
import vos.HabHost;
import vos.HabHostal;
import vos.HabHotel;
import vos.HabVivienda;
import vos.Hostal;
import vos.Hotel;
import vos.OfertaAlojamiento;
import vos.PersonaNatural;
import vos.Reserva;
import vos.ReservaColectiva;
import vos.Servicio;
import vos.ViviendaUniversitaria;

public class AlohAndesTransactionManager {

	private static final Long TOKEN_ADMIN = -1L;
	
	// -----------------------------------------------------
	// CONSTANTES
	// -----------------------------------------------------

	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos de la
	 * conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los
	 * datos de la conexion
	 */
	private static String CONNECTION_DATA_PATH;

	// ----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	// ----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String driver;

	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	private Connection conn;

	// -------------------------------
	// CONSTRUCTOR
	// -------------------------------

	/**
	 * <b>Metodo Contructor de la Clase ParranderosTransactionManager</b> <br/>
	 * <b>Postcondicion: </b> Se crea un objeto ParranderosTransactionManager, Se
	 * inicializa el path absoluto del archivo de conexion, Se inicializna los
	 * atributos para la conexion con la Base de Datos
	 * 
	 * @param contextPathP
	 *            Path absoluto que se encuentra en el servidor del contexto del
	 *            deploy actual
	 * @throws IOException
	 *             Se genera una excepcion al tener dificultades con la
	 *             inicializacion de la conexion<br/>
	 * @throws ClassNotFoundException
	 */
	public AlohAndesTransactionManager(String contextPathP) {

		try {
			System.out.println("CONTEXTPATHP: " + contextPathP);
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// -------------------------------
	// METODOS
	// -------------------------------

	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con
	 * la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * 
	 * @throws IOException
	 *             Se genera una excepcion al no encontrar el archivo o al tener
	 *             dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException
	 */
	private void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(
				new File(AlohAndesTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();

		properties.load(fileInputStream);
		fileInputStream.close();

		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");

		// Class.forName(driver);
	}

	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han
	 * sido inicializados<br/>
	 * 
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de
	 *         datos
	 * @throws SQLException
	 *             Cualquier error que se pueda llegar a generar durante la conexion
	 *             a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("[ALOHANDES APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	// ------------------------------------------------------------------------------------------
	// RF1
	// ------------------------------------------------------------------------------------------

	public void registrarPersonaNatural(PersonaNatural persona) throws Exception {

		if (!persona.getTipo().equals(TipoOperador.PADRE) && !persona.getTipo().equals(TipoOperador.EGRESADO)
				&& !persona.getTipo().equals(TipoOperador.EMPLEADO)
				&& !persona.getTipo().equals(TipoOperador.ESTUDIANTE)
				&& !persona.getTipo().equals(TipoOperador.PROFESOR) && !persona.getTipo().equals(TipoOperador.VECINO))
			throw new BusinessLogicException("El tipo no coincide");

		DAOPersonaNatural dao = new DAOPersonaNatural();
		DAOOperador daoOp = new DAOOperador();
		try {
			this.conn = darConexion();

			daoOp.setConn(conn);

			dao.setConn(conn);

			persona.setId(daoOp.generateNewId());

			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);

			daoOp.addOperador(persona);
			dao.addPersonaNatural(persona);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			System.out.println("ROLLBACK DONE");
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoOp.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarViviendaUniversitaria(ViviendaUniversitaria vivienda) throws Exception {

		if (!vivienda.getTipo().equals(TipoOperador.VIVIENDA_UNIVERSITARIA))
			throw new BusinessLogicException("El tipo no corresponde");

		DAOViviendaUniversitaria dao = new DAOViviendaUniversitaria();
		DAOOperador daoOp = new DAOOperador();

		try {
			this.conn = darConexion();

			daoOp.setConn(conn);

			dao.setConn(conn);

			vivienda.setId(daoOp.generateNewId());


			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoOp.addOperador(vivienda);
			dao.addViviendaUniversitaria(vivienda);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoOp.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarHotel(Hotel hotel) throws Exception {

		DAOHotel dao = new DAOHotel();
		DAOOperador daoOp = new DAOOperador();

		if (!hotel.getTipo().equals(TipoOperador.HOTEL))
			throw new BusinessLogicException("El tipo no corresponde");

		try {
			this.conn = darConexion();

			daoOp.setConn(conn);

			hotel.setId(daoOp.generateNewId());

			dao.setConn(conn);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);

			daoOp.addOperador(hotel);

			dao.addHotel(hotel);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoOp.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarHostal(Hostal hostal) throws Exception {

		if (!hostal.getTipo().equals(TipoOperador.HOSTAL))
			throw new BusinessLogicException("El tipo no corresponde");

		DAOHostal dao = new DAOHostal();
		DAOOperador daoOp = new DAOOperador();
		try {
			this.conn = darConexion();

			daoOp.setConn(conn);

			hostal.setId(daoOp.generateNewId());


			dao.setConn(conn);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);

			daoOp.addOperador(hostal);
			dao.addHostal(hostal);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoOp.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// RF2
	// ------------------------------------------------------------------------------------------

	public void registrarHabitacionHost(HabHost habitacion, Long token) throws Exception {

		if (!habitacion.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir una habitación a si mismo");

		if (!habitacion.getTipoAlojamiento().equals(TipoAlojamiento.HABITACION_CASA))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");

		DAOHabHost dao = new DAOHabHost();

		DAOServicio daoS = new DAOServicio();

		DAOPersonaNatural daoOp = new DAOPersonaNatural();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		try {
			this.conn = darConexion();
			daoS.setConn(conn);
			daoOp.setConn(conn);
			daoAl.setConn(conn);

			// Verificar que el operador de la hab se encuentre en la BD como PN.
			if (daoOp.findPersonaNaturalById(habitacion.getOperador()) == null)
				throw new BusinessLogicException("El dueño de la habitación debe estar en la BD");

			// A) Verifica que el servicio está en la BD
			// B) Verifica que el servicios son concordes a las reglas del negocio.
			List<Servicio> servicios = habitacion.getServicios();
			String tS;

			if (servicios != null) {
				for (Servicio s : servicios) {
					if (daoS.findServicioById(s.getId()) == null)
						throw new BusinessLogicException(
								"Uno de los servicios del alojamiento no se encuentra registrado");
					tS = s.getTipo();
					if (!tS.equals(TipoServicio.COMIDAS) && !tS.equals(TipoServicio.ACCESO_COCINA)
							&& !tS.equals(TipoServicio.BANIO_PRIVADO) && !tS.equals(TipoServicio.BANIO_COMPARTIDO))
						throw new BusinessLogicException(
								"Uno de los servicios de la habitación no es aceptada como servicio de este tipo de alojamiento");
				}
			}

			dao.setConn(conn);

			habitacion.setId(daoAl.generateNewId());

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(habitacion);

			dao.addHabHost(habitacion);
			
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoS.cerrarRecursos();
				dao.cerrarRecursos();
				daoOp.cerrarRecursos();
				daoAl.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarHabitacionHotel(HabHotel habitacion, Long token) throws Exception {

		if (habitacion.getCompartida())
			throw new BusinessLogicException("La habitación del hotel no debe ser compartida");

		if (!habitacion.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir una habitación al hotel propio.");

		if (!habitacion.getTipoAlojamiento().equals(TipoAlojamiento.HABITACION_HOTEL))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");

		String str = habitacion.getTipo();
		if (!(str.equals(TipoHabitacion.ESTANDAR) || str.equals(TipoHabitacion.SEMISUITE)
				|| str.equals(TipoHabitacion.SUITE)))
			throw new BusinessLogicException("El tipo de la habitación no coincide a ninguno de los aceptados");

		DAOHabHotel dao = new DAOHabHotel();
		DAOServicio daoS = new DAOServicio();
		DAOHotel daoH = new DAOHotel();
		DAOAlojamiento daoAl = new DAOAlojamiento();

		try {
			this.conn = darConexion();
			daoS.setConn(conn);
			daoH.setConn(conn);
			daoAl.setConn(conn);

			if (daoH.findHotelById(habitacion.getOperador()) == null)
				throw new BusinessLogicException("El dueño de la habitación debe estar registrado en la BD");

			List<Servicio> servicios = habitacion.getServicios();
			String sT;
			if (servicios != null) {
				for (Servicio s : servicios) {
					if (daoS.findServicioById(s.getId()) == null)
						throw new BusinessLogicException("Al menos uno de los servicios no está registrado en la BD");
					sT = s.getTipo();
					if (!sT.equals(TipoServicio.BANIERA) && !sT.equals(TipoServicio.SALA)
							&& !sT.equals(TipoServicio.YACUZZI) && !sT.equals(TipoServicio.COCINETA)
							&& !sT.equals(TipoServicio.RESTAURANTE) && !sT.equals(TipoServicio.PISCINA)
							&& !sT.equals(TipoServicio.WIFI) && !sT.equals(TipoServicio.TV_CABLE)
							&& !sT.equals(TipoServicio.RECEPCION_24H))
						throw new BusinessLogicException(
								"Alguno de los servicios añadidos no es válido para este tipo de alojamiento");
				}
			}

			dao.setConn(conn);

			habitacion.setId(daoAl.generateNewId());
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(habitacion);
			dao.addHabHotel(habitacion);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoAl.cerrarRecursos();
				daoH.cerrarRecursos();
				daoS.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarHabitacionHostal(HabHostal habitacion, Long token) throws Exception {

		if (!habitacion.getCompartida())
			throw new BusinessLogicException("La habitación del hostal debe ser compartida");

		if (!habitacion.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir una habitación al hotel propio");

		if (!habitacion.getTipoAlojamiento().equals(TipoAlojamiento.HABITACION_HOSTAL))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");

		DAOHabHostal dao = new DAOHabHostal();
		DAOHostal daoH = new DAOHostal();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOServicio daoS = new DAOServicio();
		try {
			this.conn = darConexion();

			daoH.setConn(conn);
			daoAl.setConn(conn);
			daoS.setConn(conn);

			if (daoH.findHostalById(habitacion.getOperador()) == null)
				throw new BusinessLogicException("El dueño de la habitación debe estar registrado en la BD");

			// Verificar que todos los servicios estén en la BD.
			List<Servicio> servicios = habitacion.getServicios();

			if (servicios != null) {
				for (Servicio s : servicios) {
					if (daoS.findServicioById(s.getId()) == null)
						throw new BusinessLogicException(
								"Al menos uno de los servicios no se encuentra registrado en la BD");
				}
			}

			habitacion.setId(daoAl.generateNewId());
			dao.setConn(conn);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(habitacion);
			dao.addHabHostal(habitacion);
			conn.commit();
		
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoS.cerrarRecursos();
				daoAl.cerrarRecursos();
				daoH.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarHabitacionVivienda(HabVivienda hb, Long token) throws Exception {

		if (!hb.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir la habitación a una vivienda propia");

		if (!hb.getTipoAlojamiento().equals(TipoAlojamiento.HABITACION_VIVIENDA_UNIV))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");

		DAOHabVivienda dao = new DAOHabVivienda();
		DAOViviendaUniversitaria daoV = new DAOViviendaUniversitaria();
		DAOServicio daoS = new DAOServicio();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		try {
			this.conn = darConexion();

			List<Servicio> servicios = hb.getServicios();

			daoAl.setConn(conn);
			daoV.setConn(conn);
			daoS.setConn(conn);

			if (daoV.findViviendaUniversitariaById(hb.getOperador()) == null)
				throw new BusinessLogicException("El dueño de la habitación debe estar registrado en la BD");

			String tS;
			boolean[] serv = new boolean[13];
			for (Servicio s : servicios) {
				tS = s.getTipo();
				if (tS.equals(TipoServicio.AMOBLADO))
					serv[0] = true;
				else if (tS.equals(TipoServicio.COCINETA))
					serv[1] = true;
				else if (tS.equals(TipoServicio.WIFI))
					serv[2] = true;
				else if (tS.equals(TipoServicio.TV_CABLE))
					serv[3] = true;
				else if (tS.equals(TipoServicio.SERVICIOS_PUBLICOS))
					serv[4] = true;
				else if (tS.equals(TipoServicio.RECEPCION_24H))
					serv[5] = true;
				else if (tS.equals(TipoServicio.ASEO))
					serv[6] = true;
				else if (tS.equals(TipoServicio.APOYO_SOCIAL))
					serv[7] = true;
				else if (tS.equals(TipoServicio.APOYO_ACADEMICO))
					serv[8] = true;
				else if (tS.equals(TipoServicio.RESTAURANTE))
					serv[9] = true;
				else if (tS.equals(TipoServicio.SALA_ESTUDIO))
					serv[10] = true;
				else if (tS.equals(TipoServicio.SALA_ESPARCIMIENTO))
					serv[11] = true;
				else if (tS.equals(TipoServicio.GIMNASIO))
					serv[12] = true;

				if (daoS.findServicioById(s.getId()) == null)
					throw new BusinessLogicException(
							"Uno de los servicios referenciados no se encuentra registrado en la BD");
			}

			// Valida que todos los servicios requeridos existan.
			boolean paraTodo = true;
			for (int i = 0; i < serv.length; i++) {
				paraTodo = paraTodo && serv[i];
			}

			if (!paraTodo) {
				throw new BusinessLogicException(
						"Al menos uno de los servicios requeridos para una vivienda universitaria no ha sido registrado");
			}

			hb.setId(daoAl.generateNewId());
			

			dao.setConn(conn);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(hb);
			dao.addHabVivienda(hb);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoAl.cerrarRecursos();
				daoS.cerrarRecursos();
				daoV.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarCasa(Casa casa, Long token) throws Exception {

		if (!casa.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir una casa a sí mismo");

		if (!casa.getTipoAlojamiento().equals(TipoAlojamiento.CASA_DIAS))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");
		if (casa.getCompartida()) {
			throw new BusinessLogicException("La casa no debería ser compartida");
		}

		DAOCasa dao = new DAOCasa();
		DAOPersonaNatural daoPN = new DAOPersonaNatural();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOServicio daoS = new DAOServicio();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			daoS.setConn(conn);
			daoPN.setConn(conn);
			daoAl.setConn(conn);

			if (daoPN.findPersonaNaturalById(casa.getOperador()) == null)
				throw new BusinessLogicException("El dueño de la casa debe estar registrado en la BD");

			List<Servicio> servicios = casa.getServicios();
			if (servicios != null) {
				for (Servicio s : servicios) {
					if (daoS.findServicioById(s.getId()) == null)
						throw new BusinessLogicException(
								"Al menos uno de los servicios no se encuentra registrado en la BD");
				}
			}

			casa.setId(daoAl.generateNewId());
			

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(casa);
			dao.addCasa(casa);
			dao.setConn(conn);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoS.cerrarRecursos();
				daoAl.cerrarRecursos();
				daoPN.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public void registrarApartamento(Apartamento apartamento, Long token) throws Exception {

		if (!apartamento.getOperador().equals(token))
			throw new BusinessLogicException("Sólo puede añadir un apartamento del que usted es dueño");

		if (!apartamento.getTipoAlojamiento().equals(TipoAlojamiento.APARTAMENTO))
			throw new BusinessLogicException("El tipo de alojamiento no corresponde");

		DAOApartamento dao = new DAOApartamento();
		DAOPersonaNatural daoPn = new DAOPersonaNatural();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOServicio daoS = new DAOServicio();

		try {
			this.conn = darConexion();
			daoPn.setConn(conn);
			daoS.setConn(conn);

			PersonaNatural duenio = daoPn.findPersonaNaturalById(apartamento.getOperador());
			if (duenio == null)
				throw new BusinessLogicException("El dueño no se encuetra registrado");
			if (duenio.getTipoPersona().equals(TipoPersona.VECINO))
				throw new BusinessLogicException("El propietario de un apartamento no puede ser un vecino");

			List<Servicio> servicios = apartamento.getServicios();

			for (Servicio s : servicios) {
				if (daoS.findServicioById(s.getId()) == null)
					throw new BusinessLogicException(
							"Al menos uno de los servicios no se encuentra registrado en la BD");
			}

			daoAl.setConn(conn);
			apartamento.setId(daoAl.generateNewId());


			dao.setConn(conn);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoAl.addAlojamiento(apartamento);
			dao.addApartamento(apartamento);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoS.cerrarRecursos();
				daoAl.cerrarRecursos();
				dao.cerrarRecursos();
				daoPn.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public OfertaAlojamiento registrarOfertaAlojamiento(OfertaAlojamiento oa, Long token) throws Exception {

		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOOfertaAlojamiento dao = new DAOOfertaAlojamiento();
		DAOHabVivienda daoC = new DAOHabVivienda();
		DAOApartamento daoA = new DAOApartamento();

		try {
			this.conn = darConexion();

			daoAl.setConn(conn);
			daoC.setConn(conn);
			daoA.setConn(conn);

			dao.setConn(conn);
			Alojamiento al = daoAl.findAlojamientoById(oa.getAlojamiento());
			if (al == null)
				throw new BusinessLogicException("No existe un alojamiento con el id dado");
			if (!(al.getOperador().equals(token)))
				throw new BusinessLogicException(
						"Sólo puede registrar ofertas que correspondan a alojamientos propios");

			Long idAl = al.getId();

			// Sólo debe permitir que alojamientos se repitan si todas las otras ofertas
			// tienen fecha retiro y fecha actual
			// after fecha retiro.
			if (dao.getOfertaWithOutRetiroOfAlojamiento(oa.getAlojamiento()) != null) {
				throw new BusinessLogicException("El alojamiento ya tiene una oferta vigente");
			}

			// After fecha retiro de mayor fechaRetiro
			if (dao.getOfertaMayorFechaRetiroOfAlojamiento(oa.getAlojamiento()) != null &&
					
					dao.getOfertaMayorFechaRetiroOfAlojamiento(oa.getAlojamiento()).getFechaRetiro()
					.after(new Date(System.currentTimeMillis()))) {
				throw new BusinessLogicException(
						"Aún no se ha retirado la oferta vigente programada a retirarse para este alojamiento");
			}

			// Si el alojamiento es una casa o apartamento, verificar que UNIDAD_COBRO es
			// mensual o
			// semestral.
			if (daoC.findHabViviendaById(idAl) != null) {
				if (!oa.getUnidadDePrecio().equals(TipoUnidadCobro.MES)
						&& !oa.getUnidadDePrecio().equals(TipoUnidadCobro.SEMESTRE))
					throw new BusinessLogicException(
							"La longitud de contrato mínima para una habitación es de un mes.");
			} else if (daoA.findApartamentoById(idAl) != null) {
				if (!oa.getUnidadDePrecio().equals(TipoUnidadCobro.MES)
						&& !oa.getUnidadDePrecio().equals(TipoUnidadCobro.SEMESTRE))
					throw new BusinessLogicException(
							"La longitud de contrato mínima para un apartamento es de un mes.");
			}
			
			oa.setFechaCreacion(new Date(System.currentTimeMillis()));
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			dao.addOfertaAlojamiento(oa);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoAl.cerrarRecursos();
				daoC.cerrarRecursos();
				dao.cerrarRecursos();
				daoA.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return oa;
	}

	// ------------------------------------------------------------------------------------------
	// RF3
	// ------------------------------------------------------------------------------------------

	public void registrarCliente(Cliente cliente) throws Exception {

		DAOCliente dao = new DAOCliente();

		try {
			this.conn = darConexion();
			
			cliente.setFechaCreacion(new Date(System.currentTimeMillis()));
			
			dao.setConn(conn);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			dao.addCliente(cliente);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// RF4
	// ------------------------------------------------------------------------------------------

//	@SuppressWarnings("deprecation")
	public void registrarUnaReserva(Reserva reserva, Long token) throws Exception {

		reserva.setEstado(TipoEstado.RESERVADA);

		System.out.println("FECHA DE LA RESERVA " + reserva.getFechaInicio());

		System.out.println("FECHA DE LA OFERTA " + reserva.getFechaCreacionOferta());

		DAOReserva dao = new DAOReserva();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		DAOOfertaAlojamiento daoOA = new DAOOfertaAlojamiento();
		DAOCasa daoC = new DAOCasa();
		if (!(reserva.getCliente().equals(token)))
			throw new BusinessLogicException("token inválido para esta acción");
		try {
			this.conn = darConexion();
			dao.setConn(conn);
			daoAl.setConn(conn);

			daoOA.setConn(conn);

			// Verificar que fechas no coincidan.

			OfertaAlojamiento oa;

			oa = daoOA.findOfertaAlojamientoByPK(reserva.getAlojamiento(), reserva.getFechaCreacionOferta());

			if (oa == null)
				throw new BusinessLogicException("La oferta debe estar en la BD");

			if (oa.getFechaCreacion().after(reserva.getFechaInicio())) {
				throw new BusinessLogicException("La oferta de alojamiento no está habilitada el día de inicio");
			}

			System.out.println("OA " + oa);

			ArrayList<Reserva> lista = dao.getReservasOfOfertaAlojamiento(oa.getAlojamiento());
			for (Reserva r : lista) {
				System.out.println("R MILIS " + r.getFechaInicio().getTime());

				if (!r.getEstado().equals(TipoEstado.CANCELADA)) {
					if (!( r.getFechaFin().before(reserva.getFechaInicio()) 
							|| reserva.getFechaFin().before(r.getFechaInicio()))) {
						throw new BusinessLogicException("La reserva coincide con alguna otra reserva ya existente");
					}
				}
			}

			Alojamiento a = daoAl.findAlojamientoById(oa.getAlojamiento());

			daoC.setConn(conn);

			// Si es una casa verificar que no exceda el límite de 30 días al año.
			if (daoC.findCasaById(a.getId()) != null) {
				double diasAcum = 0;
				long milisRes;

				Date hoy = new Date(System.currentTimeMillis());
				hoy.setYear(hoy.getYear() - 1);

				for (Reserva r : lista) {

					if (r.getFechaInicio().after(hoy)) {
						milisRes = r.getFechaFin().getTime() - r.getFechaInicio().getTime();
						diasAcum += (double) ((milisRes) / 86400000);
					}
				}

				milisRes = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
				diasAcum += (double) ((milisRes) / 86400000);

				if (diasAcum > 30.0)
					throw new BusinessLogicException(
							"La casa no puede ser reservada por la restricción de días anuales.");
			}

			// Verificar que coincida con longitud contrato.
			if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.NOCHE)) {
				long milis = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
				if ((milis - 86400000) < 0) {
					throw new BusinessLogicException(
							"La longitud mínima de acuerdo al contrato debería ser de una noche");
				}
			} else if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.SEMANA)) {
				long milis = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
				if ((milis - 604800000) < 0) {
					throw new BusinessLogicException(
							"La longitud mínima de acuerdo al contrato debería ser de una semana");
				}
			} else if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.MES)) {
				long milis = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
				if ((milis - (2592000000L)) < 0) {
					throw new BusinessLogicException("La longitud mínima de acuerdo al contrato debería ser de un mes");
				}
			} else if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.SEMESTRE)) {
				long milis = reserva.getFechaFin().getTime() - reserva.getFechaInicio().getTime();
				if ((milis - (15552000000L)) < 0) {
					throw new BusinessLogicException(
							"La longitud mínima de acuerdo al contrato debería ser de un semestre");
				}
			} else {
				throw new BusinessLogicException("Unidad de cobro del OA no reconocida");
			}

			// Verificar que el fin de la reserva sea antes de fechaRetiro (si la hay)
			if (oa.getFechaRetiro() != null) {
				if (oa.getFechaRetiro().before(reserva.getFechaFin()))
					throw new BusinessLogicException(
							"La oferta está programada para retirarse antes del fin de la reserva");
			}
			
			if(reserva.getFechaInicio().before(new Date(System.currentTimeMillis()))) {
				throw new BusinessLogicException("No se puede reservar en el pasado");
			}

			reserva.setFechaCreacion(new Date(System.currentTimeMillis()));
			

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			dao.addReserva(reserva);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoC.cerrarRecursos();
				daoOA.cerrarRecursos();
				daoAl.cerrarRecursos();
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}

		}
	}

	// ------------------------------------------------------------------------------------------
	// RF5
	// ------------------------------------------------------------------------------------------)
	public Double cancelarUnaReserva(Long idAl, Date fechaInicio, Date fechaCreacionOferta, Long token)
			throws Exception {

		DAOReserva dao = new DAOReserva();
		DAOOfertaAlojamiento daoOA = new DAOOfertaAlojamiento();
		Double costoMulta = 0.0;

		try {
			this.conn = darConexion();

			conn.setAutoCommit(false);
			dao.setConn(conn);
			daoOA.setConn(conn);

			Reserva reserva = dao.findReservaById(idAl, fechaInicio, fechaCreacionOferta);

			if (reserva == null) {
				throw new BusinessLogicException("no se encontró reserva con el id dado");
			}

			if (reserva.getEstado().equals(TipoEstado.CANCELADA)) {
				throw new BusinessLogicException("no se puede cancelar una reserva ya cancelada");
			}

			// System.out.println("RESERV C " + reserva.getCliente());
			// Verificar TOKEN.
			if (!reserva.getCliente().equals(token))
				throw new BusinessLogicException("Un cliente sólo puede cancelar sus reservas");

			OfertaAlojamiento oa = daoOA.findOfertaAlojamientoByPK(reserva.getAlojamiento(),
					reserva.getFechaCreacionOferta());

			// Fecha límite de cancelación.
			Date fechaCancelacion = new Date(reserva.getFechaInicio().getTime());
			System.out.println("ANTES MOD " + fechaCancelacion.getTime());
			if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.NOCHE)) {
				// 3 días
				System.out.println("3 días");
				fechaCancelacion.setTime(fechaCancelacion.getTime() - 259200000);
			} else {
				System.out.println("SEM");
				// 1 semana
				fechaCancelacion.setTime(fechaCancelacion.getTime() - 604800000);
			}

			System.out.println("FEcha C : " + fechaCancelacion.getTime());

			Date hoy = new Date(System.currentTimeMillis());

			System.out.println("FechaFin " + reserva.getFechaFin());
			System.out.println("Hoy " + hoy);

			if (reserva.getFechaFin().before(hoy))
				throw new BusinessLogicException("Un cliente no puede cancelar una reserva anterior");

			if (hoy.before(fechaCancelacion)) {

				costoMulta = reserva.getPrecioReserva() * 0.1;
			} else if ((hoy.after(fechaCancelacion) || hoy.equals(fechaCancelacion))
					&& hoy.before(reserva.getFechaInicio())) {
				costoMulta = reserva.getPrecioReserva() * 0.3;
			} else {

				costoMulta = reserva.getPrecioReserva() * 0.5;
			}

			System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");

			dao.cancelarReserva(reserva, costoMulta);
			conn.commit();
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				daoOA.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return costoMulta;
	}
	
	public Double cancelarUnaReservaSinCommit(Long idAl, Date fechaInicio, Date fechaCreacionOferta, Long token)
			throws Exception {

		DAOReserva dao = new DAOReserva();
		DAOOfertaAlojamiento daoOA = new DAOOfertaAlojamiento();
		Double costoMulta = 0.0;

		try {
			
			dao.setConn(conn);
			daoOA.setConn(conn);

			Reserva reserva = dao.findReservaById(idAl, fechaInicio, fechaCreacionOferta);

			if (reserva == null) {
				throw new BusinessLogicException("no se encontró reserva con el id dado");
			}

			if (reserva.getEstado().equals(TipoEstado.CANCELADA)) {
				throw new BusinessLogicException("no se puede cancelar una reserva ya cancelada");
			}

			// System.out.println("RESERV C " + reserva.getCliente());
			// Verificar TOKEN.
			if (!reserva.getCliente().equals(token))
				throw new BusinessLogicException("Un cliente sólo puede cancelar sus reservas");

			OfertaAlojamiento oa = daoOA.findOfertaAlojamientoByPK(reserva.getAlojamiento(),
					reserva.getFechaCreacionOferta());

			// Fecha límite de cancelación.
			Date fechaCancelacion = new Date(reserva.getFechaInicio().getTime());
			System.out.println("ANTES MOD " + fechaCancelacion.getTime());
			if (oa.getUnidadDePrecio().equals(TipoUnidadCobro.NOCHE)) {
				// 3 días
				System.out.println("3 días");
				fechaCancelacion.setTime(fechaCancelacion.getTime() - 259200000);
			} else {
				System.out.println("SEM");
				// 1 semana
				fechaCancelacion.setTime(fechaCancelacion.getTime() - 604800000);
			}

			System.out.println("FEcha C : " + fechaCancelacion.getTime());

			Date hoy = new Date(System.currentTimeMillis());

			System.out.println("FechaFin " + reserva.getFechaFin());
			System.out.println("Hoy " + hoy);

			if (reserva.getFechaFin().before(hoy))
				throw new BusinessLogicException("Un cliente no puede cancelar una reserva anterior");

			if (hoy.before(fechaCancelacion)) {

				costoMulta = reserva.getPrecioReserva() * 0.1;
			} else if ((hoy.after(fechaCancelacion) || hoy.equals(fechaCancelacion))
					&& hoy.before(reserva.getFechaInicio())) {
				costoMulta = reserva.getPrecioReserva() * 0.3;
			} else {

				costoMulta = reserva.getPrecioReserva() * 0.5;
			}

			dao.cancelarReservaSinCommit(reserva, costoMulta);

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}

		return costoMulta;
	}

	// ------------------------------------------------------------------------------------------
	// RF6
	// ------------------------------------------------------------------------------------------

	public void retirarOferta(OfertaAlojamiento oferta, Long token) throws Exception {

		System.out.println("OF RETIRO :" + oferta.getFechaRetiro());

		DAOOfertaAlojamiento dao = new DAOOfertaAlojamiento();
		DAOAlojamiento daoAl = new DAOAlojamiento();

		DAOReserva daoR = new DAOReserva();

		Date hoy = new Date(System.currentTimeMillis());

		if (hoy.after(oferta.getFechaRetiro()))
			throw new BusinessLogicException("no es posible retirar en el pasado");

		try {
			this.conn = darConexion();

			daoAl.setConn(conn);

			Alojamiento al = daoAl.findAlojamientoById(oferta.getAlojamiento());
			if (al == null)
				throw new BusinessLogicException("No existe un alojamiento con el id dado");
			if (!(al.getOperador().equals(token)))
				throw new BusinessLogicException(
						"Sólo puede registrar ofertas que correspondan a alojamientos propios");

			daoR.setConn(conn);

			Reserva r = daoR.getLastReservaOfOfertaAlojamiento(oferta.getAlojamiento(), oferta.getFechaCreacion());

			// Si el alojamiento tiene una última reserva.
			if (r != null) {

				if (r.getFechaFin().after(oferta.getFechaRetiro()))
					throw new BusinessLogicException(
							"La fecha de retiro no puede ser previa al fin de la última reserva");
			}
			// Alternativamente el alojamiento no tiene una última reserva y nos vale 3.


			dao.setConn(conn);
			
			OfertaAlojamiento oaBD = dao.findOfertaAlojamientoByPK(oferta.getAlojamiento(), oferta.getFechaCreacion());
			
			
			
			if(oaBD.getFechaRetiro() != null && oaBD.getFechaRetiro().before(new Date(System.currentTimeMillis()))) {
				throw new BusinessLogicException("La oferta había sido retirada previamente");
			}
			

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			
			dao.updateOfertaAlojamiento(oferta);
			conn.commit();

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				daoAl.cerrarRecursos();

				daoR.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	// -----------------------------------------------------------------------------------------
	// RF7
	// -----------------------------------------------------------------------------------------

	public ReservaColectiva addReservaColectiva (ReservaColectiva reserva, Integer necesitadas, Long token, String tipo) throws Exception {

		ReservaColectiva rpta = null;
		
		DAOReservaColectiva dao = new DAOReservaColectiva();
		if (!(reserva.getClienteId().equals(token))) {
			throw new BusinessLogicException("token inválido para esta acción");
		}
		
		if(reserva.getFechaInicio().before(new Date(System.currentTimeMillis()))) {
			throw new BusinessLogicException("No se puede hacer una reserva en el pasado");
		}
		
		try {
			this.conn = darConexion();

			ArrayList<Servicio> servicios = new ArrayList<Servicio>();

			DAOServicio daoServ = new DAOServicio();

			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			daoServ.setConn(conn);

			for (int i = 0; i < reserva.getServicios().size(); i++) {

				Servicio servicio = daoServ.findServicioByTipo(reserva.getServicios().get(i));

				if (servicio == null) {

					throw new BusinessLogicException("El servicio que ingeso no existe en la base de datos");
				}

				servicios.add(servicio);
			}
			
			dao.setConn(conn);
			
			rpta = dao.addReserva(reserva, necesitadas, servicios, tipo);
			
			conn.commit();
			
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			conn.rollback();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			conn.rollback();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return rpta;
	}
	
	// -----------------------------------------------------------------------------------------
	// RF8
	// -----------------------------------------------------------------------------------------

	public Double cancelarReservaColectiva (ReservaColectiva reserva, Long token) throws Exception {

		Double multa = 0.0;
		
		DAOReservaColectiva dao = new DAOReservaColectiva();
		
		DAOReserva daoReserva = new DAOReserva();
		
		if (!(reserva.getClienteId().equals(token))) {
			throw new BusinessLogicException("token inválido para esta acción");
		}
		try {
			
			this.conn = darConexion();

			conn.setAutoCommit(false);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			System.out.println("CONN 1" + conn);
			
			dao.setConn(conn);
			
			System.out.println("CONN 2" + conn);
			
			daoReserva.setConn(conn);
			
			
			System.out.println("ID " + reserva.getId());
			
			if(dao.findReservaColectivaById(reserva.getId()) == null) {
				throw new BusinessLogicException("No es posible cancelar una reserva que no se ha creado");
			}
			
			ArrayList<Reserva> reservas = daoReserva.getReservasOfReservaColectiva(reserva.getId());
			
			for (int i = 0; i < reservas.size(); i++) {

				multa = multa + cancelarUnaReservaSinCommit(reservas.get(i).getAlojamiento(), reservas.get(i).getFechaInicio(), reservas.get(i).getFechaCreacionOferta(), token);
			}
			
			dao.cancelarReservaColectiva(reserva);
			
			reserva.setEstado("CANCELADA");

			
			conn.commit();
			
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			conn.rollback();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			conn.rollback();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			conn.rollback();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				daoReserva.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return multa;
	}
	
	// -----------------------------------------------------------------------------------------
	// RF9
	// -----------------------------------------------------------------------------------------

	public String deshabilitarReservaAlojamiento(Long idAl, Date fechaCreacion, Date fechaDeshabilitamiento, Long token)
			throws Exception {

		DAOOfertaAlojamiento daoOa = new DAOOfertaAlojamiento();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		try {
			this.conn = darConexion();

			daoOa.setConn(conn);
			daoAl.setConn(conn);

			OfertaAlojamiento oa = daoOa.findOfertaAlojamientoByPK(idAl, fechaCreacion);

			if (oa == null) {
				throw new BusinessLogicException("No se encontró ninguna oferta de alojamiento con la PK dada");
			}

			Alojamiento a = daoAl.findAlojamientoById(idAl);
			if (!a.getOperador().equals(token)) {
				throw new BusinessLogicException("Un operador sólo puede deshabilitar su propia oferta");
			}

			// Revisar que se retirará en presente o futuro. No en pasado.
			if (fechaDeshabilitamiento != null) {
				if (fechaDeshabilitamiento.before(new Date(System.currentTimeMillis()))) {
					throw new BusinessLogicException("No se puede deshabilitar en el pasado");
				}
			}

			if (oa.getFechaRetiro() != null) {
				throw new BusinessLogicException("La oferta ya tiene un deshabilitamiento o retiro programado");
			}


			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			return daoOa.deshabilitarOfertaDeAlojamiento(idAl, fechaCreacion, fechaDeshabilitamiento);

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoAl.cerrarRecursos();
				daoOa.cerrarRecursos();
				if (this.conn != null) {

					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	// -----------------------------------------------------------------------------------------
	// RF10
	// -----------------------------------------------------------------------------------------

	public OfertaAlojamiento rehabilitarOferta(Long idAl, Date fechaCreacion, Date fechaRehabilitacion, Long token) throws Exception {
		
		DAOOfertaAlojamiento daoOa = new DAOOfertaAlojamiento();
		DAOAlojamiento daoAl = new DAOAlojamiento();
		
		try {
			this.conn = darConexion();
			
			daoOa.setConn(conn);
			daoAl.setConn(conn);
			
			OfertaAlojamiento oa = daoOa.findOfertaAlojamientoByPK(idAl, fechaCreacion);
			
			if(oa == null) {
				throw new BusinessLogicException("La oferta de alojamiento no se encuentra registrada");
			}
			
			Alojamiento a = daoAl.findAlojamientoById(idAl); 
			
			if(!a.getOperador().equals(token)) {
				throw new BusinessLogicException("Un operador sólo puede editar ofertas correspondientes a sus alojamientos");
			}
			
			if(oa.getFechaRetiro() == null) {
				throw new BusinessLogicException("No se puede rehabilitar una oferta ya habilitada");
			}
			
			if(fechaRehabilitacion == null) {
				fechaRehabilitacion=new Date(System.currentTimeMillis());
			}
			//Sólo será posible rehabilitar si fecha rehabilitación afte fechaRetiro
			if(!(fechaRehabilitacion.after(oa.getFechaRetiro()))) {
				throw new BusinessLogicException("Únicamente es posible rehabilitar una propuesta de alojamiento si fechaRehabilitacion after fechaRetiro");
			}
			
			if(daoOa.getOfertaWithOutRetiroOfAlojamiento(oa.getAlojamiento()) != null) {
				throw new BusinessLogicException("Ya existe una oferta vigente para ese alojamiento");
			}
		
			OfertaAlojamiento nuevaOfertaAlojamiento = new OfertaAlojamiento(oa.getPrecio(), null, oa.getUnidadDePrecio(), oa.getAlojamiento(), null, fechaRehabilitacion);
			

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			daoOa.addOfertaAlojamientoD(nuevaOfertaAlojamiento);
			conn.commit();
			
			return nuevaOfertaAlojamiento;

		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				daoOa.cerrarRecursos();
				daoAl.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	// ------------------------------------------------------------------------------------------
	// RFC1
	// ------------------------------------------------------------------------------------------

	@SuppressWarnings("deprecation")
	public List<RFC1> dineroRecibidoAnioActual(Long token) throws Exception {

		if (!token.equals(-1L))
			throw new BusinessLogicException("Sólo el administrador tiene acceso a este método");

		List<RFC1> lista;
		DAOReserva dao = new DAOReserva();

		// double dineroRecibido = 0;

		try {
			this.conn = darConexion();

			dao.setConn(conn);
			conn.setAutoCommit(false);
			// ArrayList<Reserva> reservas = dao.getReservas();

			Date fechaInicio = new Date(System.currentTimeMillis());
			fechaInicio.setMonth(0);
			fechaInicio.setDate(1);

			Date fechaFin = new Date(System.currentTimeMillis());

			/*
			 * for (int i = 0; i < reservas.size(); i++) {
			 * 
			 * if (reservas.get(i).getFechaFin().compareTo(fechaInicio) > 0 &&
			 * reservas.get(i).getFechaFin().compareTo(fechFin) < 0) {
			 * 
			 * dineroRecibido = dineroRecibido + reservas.get(i).getPrecioReserva(); } }
			 */

			String fi = (fechaInicio.getDate()) + "/" + (fechaInicio.getMonth() + 1) + "/"
					+ (fechaInicio.getYear() - 100);
			String ff = (fechaFin.getDate()) + "/" + (fechaFin.getMonth() + 1) + "/" + (fechaFin.getYear() - 100);

			System.out.println("F I AA " + fi);
			System.out.println("F IF AA " + ff);
			lista = dao.getDineroDates(fi, ff);
			conn.commit();

		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} /*
			 * catch (BusinessLogicException be) {
			 * System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			 * be.printStackTrace(); throw be; }
			 */catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return lista;
	}

	@SuppressWarnings("deprecation")
	public List<RFC1> dineroRecibidoAnioCorrido(Long token) throws Exception {

		DAOReserva dao = new DAOReserva();

		if (!token.equals(-1L))
			throw new BusinessLogicException("Sólo el administrador tiene acceso a este método");

		List<RFC1> lista = null;
		// double dineroRecibido = 0;

		try {
			
			
			this.conn = darConexion();

			conn.setAutoCommit(false);
			
			dao.setConn(conn);
			// ArrayList<Reserva> reservas = dao.getReservas();

			// Hace el corte por meses, primer día del mismo mes hace un año a primer día
			// del mes actual.
			Date fechaInicio = new Date(System.currentTimeMillis());
			fechaInicio.setDate(1);
			fechaInicio.setYear(fechaInicio.getYear() - 1);

			Date fechaFin = new Date(System.currentTimeMillis());
			fechaFin.setDate(1);

			/*
			 * for (int i = 0; i < reservas.size(); i++) {
			 * 
			 * if (reservas.get(i).getFechaFin().compareTo(fechaInicio) > 0 &&
			 * reservas.get(i).getFechaFin().compareTo(fechFin) < 0) {
			 * 
			 * dineroRecibido = dineroRecibido + reservas.get(i).getPrecioReserva(); } }
			 */

			String fi = (fechaInicio.getDate()) + "/" + (fechaInicio.getMonth() + 1) + "/"
					+ (fechaInicio.getYear() - 100);
			String ff = (fechaFin.getDate()) + "/" + (fechaFin.getMonth() + 1) + "/" + (fechaFin.getYear() - 100);

			System.out.println("F I AC " + fi);
			System.out.println("F IF A " + ff);
			lista = dao.getDineroDates(fi, ff);
			conn.commit();

		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} /*
			 * catch (BusinessLogicException be) {
			 * System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			 * be.printStackTrace(); throw be; }
			 */catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return lista;
	}

	// ------------------------------------------------------------------------------------------
	// RFC2
	// ------------------------------------------------------------------------------------------

	public List<OfertaAlojamiento> getOfertasMasPopulares(Integer n) throws Exception {

		List<OfertaAlojamiento> ofertas;

		DAOOfertaAlojamiento dao = new DAOOfertaAlojamiento();

		try {
			
			
			this.conn = darConexion();

			conn.setAutoCommit(false);
			
			dao.setConn(conn);

			ofertas = dao.getOfertasMasPopulares();
			
			conn.commit();

		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (BusinessLogicException be) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + be.getMessage());
			be.printStackTrace();
			throw be;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		System.out.println("HOLA TAM ES " + ofertas.size());
		if (n < ofertas.size()) {
			System.out.println("N MENOR");
			return ofertas.subList(0, n);
		} else {
			System.out.println("N MENOR");
			return ofertas;
		}
	}

	// ------------------------------------------------------------------------------------------
	// RFC3
	// ------------------------------------------------------------------------------------------

	public List<RFC3> getIndiceOcupacion() throws SQLException {

		DAOOfertaAlojamiento dao = new DAOOfertaAlojamiento();

		List<RFC3> indices = new ArrayList<RFC3>();

		try {
			this.conn = darConexion();

			conn.setAutoCommit(false);
			
			dao.setConn(conn);

			indices = dao.getIndiceOcupacion();
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return indices;
	}

	// ------------------------------------------------------------------------------------------
	// RFC4
	// ------------------------------------------------------------------------------------------

	public List<Alojamiento> getAlojamientosEntreFechasConServicios(Date fechaInicio, Date fechaFin, List<String> servi)
			throws Exception {

		ArrayList<Alojamiento> alojamientos = new ArrayList<Alojamiento>();

		try {
			this.conn = darConexion();

			ArrayList<Servicio> servicios = new ArrayList<Servicio>();

			DAOServicio daoServ = new DAOServicio();

			daoServ.setConn(conn);
			
			conn.setAutoCommit(false);
			
			for (int i = 0; i < servi.size(); i++) {

				Servicio servicio = daoServ.findServicioByTipo(servi.get(i));

				if (servicio == null) {

					throw new BusinessLogicException("El servicio que ingeso no existe en la base de dato");
				}

				servicios.add(servicio);
			}

			DAOAlojamiento dao = new DAOAlojamiento();

			dao.setConn(conn);

			alojamientos = (ArrayList<Alojamiento>) dao.getAlojamientosEntreFechasConServicios(fechaInicio, fechaFin,
					servicios);
			
			conn.commit();

		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return alojamientos;
	}

	// ------------------------------------------------------------------------------------------
	// RFC5
	// ------------------------------------------------------------------------------------------

	public List<UsoTipoCliente> getUsoTipoCliente() throws Exception {

		DAOCliente dao = new DAOCliente();
		List<UsoTipoCliente> resp = new ArrayList<UsoTipoCliente>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			conn.setAutoCommit(false);
			
			resp = dao.getUsoTipoCliente();

			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}

	// ------------------------------------------------------------------------------------------
	// RFC6
	// ------------------------------------------------------------------------------------------

	public List<UsoCliente> getUsoCliente(Long idCliente) throws Exception {

		DAOCliente dao = new DAOCliente();
		List<UsoCliente> resp = new ArrayList<UsoCliente>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);

			
			conn.setAutoCommit(false);
			resp = dao.getUsoCliente(idCliente);
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}
	
	// ------------------------------------------------------------------------------------------
	// RFC7
	// ------------------------------------------------------------------------------------------
	
		public List<Log> analizarOperacion(String unidadTiempo, String tipoAl) throws Exception{
			
			System.out.println("LLEGOO 2");
			
			DAOReserva dao = new DAOReserva();
			List<Log> resp = new ArrayList<Log>();
			try {
				this.conn = darConexion();

				dao.setConn(conn);

				if(!(unidadTiempo.equals(TipoUnidadTiempo.ANIO) || unidadTiempo.equals(TipoUnidadTiempo.MES)
						|| unidadTiempo.equals(TipoUnidadTiempo.SEMANA) || unidadTiempo.equals(TipoUnidadTiempo.DIA))) {
					throw new BusinessLogicException("La unidad de tiempo no es ninguna de las predeterminadas");
				}
				
				if(!( tipoAl.equals(TipoAlojamiento.APARTAMENTO) || tipoAl.equals(TipoAlojamiento.CASA_DIAS)
						|| tipoAl.equals(TipoAlojamiento.HABITACION_CASA) || tipoAl.equals(TipoAlojamiento.HABITACION_HOSTAL) 
						|| tipoAl.equalsIgnoreCase(TipoAlojamiento.HABITACION_HOTEL) || tipoAl.equals(TipoAlojamiento.HABITACION_VIVIENDA_UNIV))) {
					throw new BusinessLogicException("El tipo de alojamiento no coincide con ningno de los permitidos");
				}
				
				conn.setAutoCommit(false);
				resp = dao.getUnidadConMayor(tipoAl, unidadTiempo);
				conn.commit();

			} catch (SQLException sqlException) {
				
				conn.rollback();
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} finally {
				try {
					dao.cerrarRecursos();
					if (this.conn != null) {
						this.conn.close();
					}
				} catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return resp;
		}
	
	// ------------------------------------------------------------------------------------------
	// RFC8
	// ------------------------------------------------------------------------------------------
	
		public List<Cliente> getClientesFrecuentes(Long idAl) throws Exception{
		
		DAOCliente dao = new DAOCliente();
		List<Cliente> resp = new ArrayList<Cliente>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			conn.setAutoCommit(false);
			
			resp = dao.getClientesFrecuentesOfAl(idAl);
			
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return resp;
		
	}
	
	// ------------------------------------------------------------------------------------------
	// RFC9
	// ------------------------------------------------------------------------------------------

	public List<Alojamiento> getAlojamientoBajaDemanda() throws Exception{
		
		DAOAlojamiento dao = new DAOAlojamiento();
		List<Alojamiento> resp = new ArrayList<Alojamiento>();
		try {
			this.conn = darConexion();

			
			
			dao.setConn(conn);
			
			conn.setAutoCommit(false);
			
			resp = dao.getAlojamientosBajaDemanda();
			
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return resp;
		
	}
	
	//-----------------------------------------------------------------
	//RFC10
	//-----------------------------------------------------------------
	
	private boolean determinarValidezAgrupamientoOrdenamiento(Integer agrupamiento, Integer ordenamiento) {
		
		if((agrupamiento != null && agrupamiento < 0) || (ordenamiento != null && ordenamiento < 0))
			return false;
		
		//Revisar tama�os
		if(agrupamiento != null && agrupamiento > TipoAgrupamiento.TIPOS_AGRUPAMIENTOS.length-1)
			return false;
		if(ordenamiento != null && ordenamiento > TipoOrdenamiento.TIPOS_AGRUPADO.length && ordenamiento > TipoOrdenamiento.TIPOS_DESAGRUPADO.length-1)
			return false;
		
		if(agrupamiento != null && ordenamiento != null && ordenamiento > TipoOrdenamiento.TIPOS_AGRUPADO.length)
			return false;
		
		return true;
	}
	
	private String determinarAgrupamiento(Integer agrupamiento) {
		
		if(agrupamiento == null)
			return null;
		
		return TipoAgrupamiento.TIPOS_AGRUPAMIENTOS[agrupamiento];
		
	}
	
	private String determinarOrdenamiento(Integer ordenamiento, boolean hayAgrupamiento) {
		
		if(ordenamiento == null) {
			return null;
		}
		
		if(hayAgrupamiento) {
			return TipoOrdenamiento.TIPOS_AGRUPADO[ordenamiento];
		}	
		else {
			return TipoOrdenamiento.TIPOS_DESAGRUPADO[ordenamiento];
		}
		
	}
	
	public List<RFC10> darClientesConReservaEnRangoAgrupado(Long idAloj, Date cotaInferior, Date cotaSuperior, Long token, Integer agrupamiento, Integer ordenamiento) throws Exception{
		
		validarToken(idAloj, token);
		
		DAOCliente dao = new DAOCliente();
		
		List<RFC10> resp = new ArrayList<RFC10>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			if(!determinarValidezAgrupamientoOrdenamiento(agrupamiento, ordenamiento))
				throw new BusinessLogicException("La combinaci�n agrupamiento/ordenamiento usada, no es v�lida");
			
			
			conn.setAutoCommit(false);
			resp = dao.getClientesConReservaEnRangoAgrupando(idAloj, cotaInferior, cotaSuperior, determinarAgrupamiento(agrupamiento), determinarOrdenamiento(ordenamiento, true));
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}
	
	public List<Cliente> darClientesConReservaEnRango(Long idAloj, Date cotaInferior, Date cotaSuperior, Long token, Integer ordenamiento) throws Exception{
				
		validarToken(idAloj, token);
		
		DAOCliente dao = new DAOCliente();
		
		List<Cliente> resp = new ArrayList<Cliente>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			if(!determinarValidezAgrupamientoOrdenamiento(null, ordenamiento))
				throw new BusinessLogicException("La combinaci�n agrupamiento/ordenamiento usada, no es v�lida");
			
			conn.setAutoCommit(false);
			resp = dao.getClientesConReservaEnRango(idAloj, cotaInferior, cotaSuperior, determinarOrdenamiento(ordenamiento, false));
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}

	//-----------------------------------------------------------------
	//RFC11
	//-----------------------------------------------------------------
		
	private void validarToken(Long idAloj, Long token) throws Exception{
		
		DAOAlojamiento dao = new DAOAlojamiento();
		try {
			
			this.conn = darConexion();
			
			conn.setAutoCommit(false);
			
			dao.setConn(conn);
			
			Alojamiento al = dao.findAlojamientoById(idAloj);
			
			if(al == null) {
				throw new BusinessLogicException("El alojamiento dado no existe");
			}
			else if(!al.getOperador().equals(token) && !token.equals(TOKEN_ADMIN)) {
				throw new BusinessLogicException("No tiene permisos para realizar esto");
			}
		}catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		}catch (BusinessLogicException exception) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<Cliente> darClientesSinReservaEnRango(Long idAloj, Date cotaInferior, Date cotaSuperior, Long token, Integer ordenamiento) throws Exception{

		
		validarToken(idAloj, token);
		
		
		DAOCliente dao = new DAOCliente();
		List<Cliente> resp = new ArrayList<Cliente>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			if(!determinarValidezAgrupamientoOrdenamiento(null, ordenamiento)) {
				throw new BusinessLogicException("La combinaci�n ordenamiento / agrupamiento no es v�lida");
			}
			
			conn.setAutoCommit(false);
			resp = dao.getClientesSinReservaEnRango(idAloj, cotaInferior, cotaSuperior, determinarOrdenamiento(ordenamiento, false));
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		}catch (BusinessLogicException exception) {
			System.err.println("[EXCEPTION] BusinessLogicException: " + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		}
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}
	
	public List<RFC10> darClientesSinReservaEnRangoAgrupado(Long idAloj, Date cotaInferior, Date cotaSuperior, Long token, Integer agrupamiento, Integer ordenamiento) throws Exception{
		
		validarToken(idAloj, token);
		
		DAOCliente dao = new DAOCliente();
		
		List<RFC10> resp = new ArrayList<RFC10>();
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			if(!determinarValidezAgrupamientoOrdenamiento(agrupamiento, ordenamiento))
				throw new BusinessLogicException("La combinaci�n agrupamiento/ordenamiento usada, no es v�lida");
			
			conn.setAutoCommit(false);
			resp = dao.getClientesSinReservaEnRangoAgrupando(idAloj, cotaInferior, cotaSuperior, determinarAgrupamiento(agrupamiento), determinarOrdenamiento(ordenamiento, true));
			conn.commit();
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return resp;
	}
	

	//-----------------------------------------------------------------
	//RFC12
	//-----------------------------------------------------------------
	
	public List<RFC12> darFuncionamiento(Long token, Integer anio) throws Exception{
		
		if(!token.equals(TOKEN_ADMIN)) {
			throw new BusinessLogicException("�nicamente el administrador puede realizar esta consulta");
		}
		
		DAOFuncionamiento dao = new DAOFuncionamiento();
		List<RFC12> resp = new ArrayList<RFC12>();
		
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			resp = dao.getFuncionamiento(anio);
			
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return resp;

	}
	
	//-----------------------------------------------------------------
	//RFC13
	//-----------------------------------------------------------------
	
	public List<RFC13> darBuenosClientes(Long token) throws Exception{
		
		if(!token.equals(TOKEN_ADMIN)) {
			throw new BusinessLogicException("�nicamente el administrador puede realizar esta consulta");
		}
		
		DAOCliente dao = new DAOCliente();
		List<RFC13> resp = new ArrayList<RFC13>();
		
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			conn.setAutoCommit(false);
			resp = dao.getBuenosClientes();
			conn.commit();
			
		} catch (SQLException sqlException) {
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		
		return resp;

	}
	
	
	
	public void poblarTabla() throws Exception{
		
		DAONumSemanas dao = new DAONumSemanas();
		
		try {
			this.conn = darConexion();

			dao.setConn(conn);
			
			dao.poblarTabla();
			
		} catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} finally {
			try {
				dao.cerrarRecursos();
				if (this.conn != null) {
					this.conn.close();
				}
			} catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
}
