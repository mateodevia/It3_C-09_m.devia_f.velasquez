package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class HabHost extends Alojamiento {
	
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value="numComparte")
	private Integer numComparte;

    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
    public HabHost(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")Boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas,
    		@JsonProperty(value="numComparte")Integer numComparte,
    		@JsonProperty(value="operador")Long duenio) {
    	
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas, duenio);
		this.numComparte = numComparte;
	}
    
    //------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
    
	public Integer getNumComparte() {
		return numComparte;
	}

	public void setNumComparte(Integer numComparte) {
		this.numComparte = numComparte;
	}

    /**
     * Constructor por defecto.
     */
	public HabHost() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
}
