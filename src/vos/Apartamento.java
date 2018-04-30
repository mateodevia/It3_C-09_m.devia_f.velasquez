package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Apartamento extends Alojamiento {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
	public Apartamento(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas, @JsonProperty(value="duenio")long duenio) {
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas,duenio);
	}
    /**
     * Constructor por defecto.
     */
	public Apartamento() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
