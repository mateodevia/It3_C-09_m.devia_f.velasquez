package vos;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservaColectiva {

	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value="id")
	private Long id;
	
	@JsonProperty(value="evento")
	private String evento;
	
	@JsonProperty(value="reservas")
	private List<Reserva> reservas;
	
	@JsonProperty(value="servicios")
	private List <String> servicios;
	
	@JsonProperty(value="estado")
	private String estado;
	
	@JsonProperty(value="fechaInicio")
	private Date fechaInicio;
	
	@JsonProperty(value="fechFin")
	private Date fechaFin;
	
	@JsonProperty(value="tipoContrato")
	private String tipoContrato;
	
	@JsonProperty(value="numPersonasPorReserva")
	private Integer numPersonasPorReserva;
	
	@JsonProperty(value="clienteId")
	private Long clienteId;
	
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------

	public ReservaColectiva(@JsonProperty(value="id")Long id, @JsonProperty(value="evento")String evento, @JsonProperty(value="reservas")List<Reserva> reservas, @JsonProperty(value="servicios")List<String> servicios, @JsonProperty(value="estado")String estado,
				@JsonProperty(value="fechaInicio")Date fechaInicio,
				@JsonProperty(value="fechFin")Date fechaFin,	
				@JsonProperty(value="tipoContrato")String tipoContrato,	
				@JsonProperty(value="numPersonasPorReserva")Integer numPersonasPorReserva,	
				@JsonProperty(value="clienteId")Long clienteId) {
		super();
		this.id = id;
		this.evento = evento;
		this.reservas = reservas;
		this.servicios = servicios;
		this.estado = estado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.tipoContrato = tipoContrato;
		this.numPersonasPorReserva = numPersonasPorReserva;
		this.clienteId = clienteId;
		
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public List<String> getServicios() {
		return servicios;
	}

	public void setServicios(List<String> servicios) {
		this.servicios = servicios;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public Integer getNumPersonasPorReserva() {
		return numPersonasPorReserva;
	}

	public void setNumPersonasPorReserva(Integer numPersonasPorReserva) {
		this.numPersonasPorReserva = numPersonasPorReserva;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}
	
}