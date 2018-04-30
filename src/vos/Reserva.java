package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Reserva {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value = "fechaCreacion")
	private Date fechaCreacion;
	
    /**
    * @generated
    */
	@JsonProperty(value="fechaInicio")
    private Date fechaInicio;
    
    /**
    * @generated
    */
	@JsonProperty(value="fechaFin")
    private Date fechaFin;
    
    /**
    * @generated
    */
	@JsonProperty(value="tipoContrato")
    private String tipoContrato;
    
    /**
    * @generated
    */
	@JsonProperty(value="numPersonas")
    private Integer numPersonas;
    
    /**
    * @generated
    */
	@JsonProperty(value="precioReserva")
    private Double precioReserva;
      
    /**
    * @generated
    */
	//este modela el ID_AL_OF de la BD
	@JsonProperty(value="alojamiento")
    private Long alojamiento;
	
	@JsonProperty(value="fechaCreacionOferta")
	private Date fechaCreacionOferta;
    
    /**
    * @generated
    */
	@JsonProperty(value="cliente")
    private Long cliente;
	
	@JsonProperty( value = "estado" )
	private String estado;
	
	@JsonProperty( value = "reservaColectiva" )
	private Long reservaColectiva;
	
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public Reserva(@JsonProperty(value="fechaInicio")Date fechaInicio, @JsonProperty(value="fechaFin")Date fechaFin,
			@JsonProperty(value="tipoContrato")String tipoContrato, @JsonProperty(value="numPersonas")Integer numPersonas,
			@JsonProperty(value="precioReserva")Double precioReserva,
			@JsonProperty(value="alojamiento")Long alojamiento, 
			@JsonProperty(value="fechaCreacionOferta")Date fechaCreacionOferta,
			@JsonProperty(value="cliente")Long cliente, @JsonProperty(value="estado")String estado,
			@JsonProperty( value = "reservaColectiva" )Long reservaColectiva,
			@JsonProperty(value = "fechaCreacion") Date fechaCreacion) {

		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.tipoContrato = tipoContrato;
		this.numPersonas = numPersonas;
		this.precioReserva = precioReserva;
		this.alojamiento = alojamiento;
		this.fechaCreacionOferta = fechaCreacionOferta;
		this.cliente = cliente;
		this.estado = estado;
		this.reservaColectiva = reservaColectiva;
		this.fechaCreacion = fechaCreacion;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public Date getFechaInicio() {
        return this.fechaInicio;
    }

	/**
    * @generated
    */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    
    /**
    * @generated
    */
    public Date getFechaFin() {
        return this.fechaFin;
    }
    
    /**
    * @generated
    */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    
    /**
    * @generated
    */
    public String getTipoContrato() {
        return this.tipoContrato;
    }
    
    /**
    * @generated
    */
    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
    
    
    /**
    * @generated
    */
    public int getNumPersonas() {
        return this.numPersonas;
    }
    
    /**
    * @generated
    */
    public void setNumPersonas(Integer numPersonas) {
        this.numPersonas = numPersonas;
    }
    
    
    /**
    * @generated
    */
    public double getPrecioReserva() {
        return this.precioReserva;
    }
    
    /**
    * @generated
    */
    public void setPrecioReserva(Double precioReserva) {
        this.precioReserva = precioReserva;
    }
    
    /**
    * @generated
    */
    public Long getCliente() {
        return this.cliente;
    }
    
    /**
    * @generated
    */
    public void setCliente(long cliente) {
        this.cliente = cliente;
    }
    
    
    /**
    * @generated
    */
    public long getAlojamiento() {
        return this.alojamiento;
    }
    
    /**
    * @generated
    */
    public void setAlojamiento(long alojamiento) {
        this.alojamiento = alojamiento;
    }

    public String getEstado() {
		return estado;
	}
    
    public void setEstado(String estado) {
		this.estado = estado;
	}
    
    
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	
	public Date getFechaCreacionOferta() {
		return fechaCreacionOferta;
	}
	
	public void setFechaCreacionOferta(Date fechaCreacionOferta) {
		this.fechaCreacionOferta = fechaCreacionOferta;
	}

	public Long getReservaColectiva() {
		return reservaColectiva;
	}

	public void setReservaColectiva(Long reservaColectiva) {
		this.reservaColectiva = reservaColectiva;
	}

	/**
     * Constructor por defecto
     */
	public Reserva() {
		
	}
    
	
	
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
}
