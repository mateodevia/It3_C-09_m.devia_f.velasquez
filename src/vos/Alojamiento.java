package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/

public class Alojamiento {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value="id")
	private Long id;
	
    /**
    * @generated
    */
	@JsonProperty(value="capacidad")
    private Integer capacidad;
    
    /**
    * @generated
    */
	@JsonProperty(value="compartida")
    private Boolean compartida;
    
    /**
    * @generated
    * tipoAlojamiento IN TipoAlojamiento.*
    */
	@JsonProperty(value="tipoAlojamiento")
    private String tipoAlojamiento;
    
    /**
    * @generated
    */
	@JsonProperty(value="ubicacion")
    private String ubicacion;
    
    /**
    * @generated
    */
	@JsonProperty(value="servicios")
    private List<Servicio> servicios;
    
	@JsonProperty(value="ofertas")
    private List<OfertaAlojamiento> ofertas;
	
	@JsonProperty(value = "operador")
	private Long operador;
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	
	public Alojamiento(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas, @JsonProperty(value = "operador") Long operador ) {
		
		this.operador = operador;
		this.id = id;
		this.capacidad = capacidad;
		this.compartida = compartida;
		this.tipoAlojamiento = tipoAlojamiento;
		this.ubicacion = ubicacion;
		this.servicios = servicios;
		this.ofertas = ofertas;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setter
    //------------------------------------------------------------------------------------------

    public List<OfertaAlojamiento> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<OfertaAlojamiento> oferta) {
		this.ofertas = oferta;
	}

	/**
    * @generated
    */
    public int getCapacidad() {
        return this.capacidad;
    }
    
    /**
    * @generated
    */
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
    
    
    /**
    * @generated
    */
    public boolean getCompartida() {
        return this.compartida;
    }
    
    /**
    * @generated
    */
    public void setCompartida(Boolean compartida) {
        this.compartida = compartida;
    }
    
    
    /**
    * @generated
    */
    public String getTipoAlojamiento() {
        return this.tipoAlojamiento;
    }
    
    /**
    * @generated
    */
    public void setTipoAlojamiento(String tipoAlojamiento) {
        this.tipoAlojamiento = tipoAlojamiento;
    }
    
    
    /**
    * @generated
    */
    public String getUbicacion() {
        return this.ubicacion;
    }
    
    /**
    * @generated
    */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    
    
    /**
    * @generated
    */
    public List<Servicio> getServicios() {
        return this.servicios;
    }
    
    /**
    * @generated
    */
    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }
    
    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
		this.id = id;
	}

    /**
     * Constructor por defecto.
     */
	public Alojamiento() {
		
	}

	public Long getOperador() {
		return operador;
	}

	public void setOperador(Long operador) {
		this.operador = operador;
	}
	
	
	
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
