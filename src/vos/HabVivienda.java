package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
* Representa la habitación de una vivienda universitaria. 
*/
public class HabVivienda extends Alojamiento {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="numeroHabitacion")
    private Integer numeroHabitacion;
    
    /**
    * @generated
    */
	@JsonProperty(value="numeroComparte")
    private Integer numeroComparte;
    
    
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------

	public HabVivienda(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas,
			@JsonProperty(value="numeroHabitaciones")Integer numeroHabitacion,
			@JsonProperty(value="numeroComparte")Integer numeroComparte, @JsonProperty(value="viviendaUniversitaria")Long viviendaUniversitaria) {
		
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas, viviendaUniversitaria);
		this.numeroHabitacion = numeroHabitacion;
		this.numeroComparte = numeroComparte;
	}
	
    //------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
    
    /**
    * @generated
    */
    public Integer getNumeroHabitacion() {
        return this.numeroHabitacion;
    }
    
    /**
    * @generated
    */
    public void setNumeroHabitacion(Integer numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }
    
    
    /**
    * @generated
    */
    public Integer getNumeroComparte() {
        return this.numeroComparte;
    }
    
    /**
    * @generated
    */
    public void setNumeroComparte(Integer numeroComparte) {
        this.numeroComparte = numeroComparte;
    }
    

    /**
     * Constructor por defecto.
     */
	public HabVivienda() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
