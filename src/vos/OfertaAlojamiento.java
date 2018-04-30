package vos;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class OfertaAlojamiento{
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="precio")
    private Double precio;
    
    /**
    * @generated
    */
	@JsonProperty(value="fechaRetiro")
    private Date fechaRetiro;
    
    /**
    * @generated
    */
	@JsonProperty(value="unidadDePrecio")
    private String unidadDePrecio;
    
    
    /**
    * @generated
    */
	@JsonProperty(value="alojamiento")
    private Long alojamiento;
    
	@JsonProperty(value="reservas")
    private List<Reserva> reservas; 
	
	@JsonProperty(value = "fechaCreacion")
	private Date fechaCreacion;

	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public OfertaAlojamiento(@JsonProperty(value="precio")Double precio, @JsonProperty(value="fechaRetiro")Date fechaRetiro, @JsonProperty(value="unidadDePrecio")String unidadDePrecio, @JsonProperty(value="Alojamiento")long alojamiento,
			@JsonProperty(value="reservas")List<Reserva> reservas, @JsonProperty(value="fechaCreacion") Date fechaCreacion) {
		
		this.precio = precio;
		this.fechaRetiro = fechaRetiro;
		this.unidadDePrecio = unidadDePrecio;
		this.alojamiento = alojamiento;
		this.reservas = reservas;
		this.fechaCreacion = fechaCreacion;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
	
    public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	/**
    * @generated
    */
    public Double getPrecio() {
        return this.precio;
    }
    
    /**
    * @generated
    */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
    
    /**
    * @generated
    */
    public Date getFechaRetiro() {
        return this.fechaRetiro;
    }
    
    /**
    * @generated
    */
    public void setFechaRetiro(Date fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }
    
    
    /**
    * @generated
    */
    public String getUnidadDePrecio() {
        return this.unidadDePrecio;
    }
    
    /**
    * @generated
    */
    public void setUnidadDePrecio(String unidadDePrecio) {
        this.unidadDePrecio = unidadDePrecio;
        
    }

	public long getAlojamiento() {
		return alojamiento;
	}

	public void setAlojamiento(long alojamiento) {
		this.alojamiento = alojamiento;
	}
	
	 public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
     * Constructor por defecto.
     */
	public OfertaAlojamiento() {
		
	}
    
}
