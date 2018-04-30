package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class HabHotel extends Alojamiento {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="numero")
    private Integer numero;
    
    /**
    * @generated
    */
	@JsonProperty(value="tipo")
    private String tipo;
    
    /**
    * @generated
    */
	@JsonProperty(value="tamanio")
    private Double tamanio;
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public HabHotel(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas,
			@JsonProperty(value="numero")Integer numero, @JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="tamanio")Double tamanio, @JsonProperty(value="hotel")Long hotel) {
		
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas, hotel);
		this.numero = numero;
		this.tipo = tipo;
		this.tamanio = tamanio;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
    public Integer getNumero() {
        return this.numero;
    }
    
    /**
    * @generated
    */
    public void setNumero(Integer numero) {
        this.numero = numero;
    }
        
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
    public Double getTamanio() {
        return this.tamanio;
    }
    
    /**
    * @generated
    */
    public void setTamanio(Double tamanio) {
        this.tamanio = tamanio;
    }
  
    
    /**
     * Constructor por defecto.
     */
	public HabHotel() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
