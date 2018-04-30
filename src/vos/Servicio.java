package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


/**
* @generated
*/
public class Servicio {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value="id")
	private Long id;
	
    /**
    * @generated
    * Tipo IN TipoServicio.*
    */
	@JsonProperty(value="tipo")
    private String tipo;
  
	
    /**
    * @generated
    */
	@JsonProperty(value="costo")
    private Double costo;
	
	@JsonProperty( value = "reservasColectivas" )
	private List<Long> reservasColectivas;
    
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
	public Servicio(@JsonProperty(value="id")Long id, @JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="costo")Double costo,
			@JsonProperty( value = "reservasColectivas" )List<Long> reservasColectivas) {
		
		this.id = id;
		this.tipo = tipo;
		this.costo = costo;
		this.reservasColectivas = reservasColectivas;
	}
    
    //------------------------------------------------------------------------------------------
    // Geters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public String getTipo() {
        return this.tipo;
    }
    
	/**
    * @generated
    */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    /**
    * @generated
    */
    public Double getCosto() {
        return this.costo;
    }
    
    /**
    * @generated
    */
    public void setCosto(Double costo) {
        this.costo = costo;
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
	public Servicio() {
		
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
	
}
