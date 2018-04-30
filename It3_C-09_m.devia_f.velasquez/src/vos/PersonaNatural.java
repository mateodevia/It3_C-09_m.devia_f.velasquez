package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class PersonaNatural extends Operador{

	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	/**
	 * Representa el tipo de persona.
	 * tipoPersona IN {TipoPersona.VECINO, TipoPersona.MIEMBRO_COMUNIDAD}
	 */
	@JsonProperty(value="tipoPersona")
	private String tipoPersona;
	
	/**
	 * Representa el apellido
	 */
	@JsonProperty(value="apellido")
	private String apellido;
	
	/**
	 * Representa el tipo de documento
	 * tipoDocumento IN {TipoDocumento.CEDULA_CIUDADANIA, TipoDocumento.TARJETA_IDENTIDAD, TipoDocumento.CEDULA_EXTRANJERIA, TipoDocumento.PASAPORTE}
	 */
	@JsonProperty(value="tipoDocumento")
	private String tipoDocumento; 
	
	
	/**
	 * Representa el número de documento de la persona natural. 
	 * numDocumento > 0
	 */
	@JsonProperty(value="numDocumento")
	private Long numDocumento; 
	
	
	/**
	 * Representa las habitaciones que esta persona ofrece.
	 */
	@JsonProperty(value="habHost")
	private List<HabHost> habsHost;
	
	/**
	 * Representa las casas por días que esta persona ofrece.
	 */
	@JsonProperty(value="casas")
	private List<Casa> casas; 
	
	/**
	 * Representa los apartamentos (por meses) que ofrece esta persona.
	 */
	@JsonProperty(value="apartamentos")
	private List<Apartamento> apartamentos;

	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public PersonaNatural(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="tipoPersona")String tipoPersona,
			@JsonProperty(value="apellido")String apellido,
			@JsonProperty(value="tipoDocumento")String tipoDocumento, @JsonProperty(value="numDocumento")Long numDocumento, @JsonProperty(value="hadbHost")List<HabHost> habsHost, @JsonProperty(value="casas")List<Casa> casas,
			@JsonProperty(value="apartamentos")List<Apartamento> apartamentos) {
		
		super(id, nombre, tipo);		
		this.tipoPersona = tipoPersona;
		this.apellido = apellido;
		this.tipoDocumento = tipoDocumento;
		this.numDocumento = numDocumento;
		this.habsHost = habsHost;
		this.casas = casas;
		this.apartamentos = apartamentos;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
	
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Long getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(Long numDocumento) {
		this.numDocumento = numDocumento;
	}

	public List<HabHost> getHabsHost() {
		return habsHost;
	}

	public void setHabsHost(List<HabHost> habsHost) {
		this.habsHost = habsHost;
	}

	public List<Casa> getCasas() {
		return casas;
	}

	public void setCasas(List<Casa> casas) {
		this.casas = casas;
	}

	public List<Apartamento> getApartamentos() {
		return apartamentos;
	}

	public void setApartamentos(List<Apartamento> apartamentos) {
		this.apartamentos = apartamentos;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	/**
	 * Constructor por defecto.
	 */
	public PersonaNatural() {
		super();
	}
	
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
}
