package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class HabHostal extends Alojamiento {
    
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
	@JsonProperty(value="numComparte")
    private Integer numComparte;
   
    
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------

	public HabHostal(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas,
			@JsonProperty(value="numero")Integer numero, @JsonProperty(value="numComparte")Integer numComparte,
			@JsonProperty(value="hostal")Long hostal) {
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas, hostal);
		this.numero = numero;
		this.numComparte = numComparte;
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
    public Integer getNumComparte() {
        return this.numComparte;
    }
    
    /**
    * @generated
    */
    public void setNumComparte(Integer numComparte) {
        this.numComparte = numComparte;
    }

    /**
     * Constructor por defecto.
     */
	public HabHostal() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
