package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Casa extends Alojamiento {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="menaje")
    private String menaje;
    
    /**
    * @generated
    */
	@JsonProperty(value="habitaciones")
    private Integer habitaciones;
    
    /**
    * @generated
    */
	@JsonProperty(value="seguro")
    private String seguro;
   
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
    public Casa(@JsonProperty(value="id")Long id, @JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="compartida")boolean compartida, @JsonProperty(value="tipoAlojamiento")String tipoAlojamiento, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="servicios")List<Servicio> servicios, @JsonProperty(value="ofertas")List<OfertaAlojamiento> ofertas,
    		@JsonProperty(value="menaje")String menaje, @JsonProperty(value="habitaciones")Integer habitaciones, @JsonProperty(value="seguro")String seguro,
    		@JsonProperty(value="duenio")Long duenio) {
		super(id, capacidad, compartida, tipoAlojamiento, ubicacion, servicios, ofertas,duenio);
		this.menaje = menaje;
		this.habitaciones = habitaciones;
		this.seguro = seguro;
	}
    
    //------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public String getMenaje() {
        return this.menaje;
    }

	/**
    * @generated
    */
    public void setMenaje(String menaje) {
        this.menaje = menaje;
    }
    
    
    /**
    * @generated
    */
    public Integer getHabitaciones() {
        return this.habitaciones;
    }
    
    /**
    * @generated
    */
    public void setHabitaciones(Integer habitaciones) {
        this.habitaciones = habitaciones;
    }
    
    
    /**
    * @generated
    */
    public String getSeguro() {
        return this.seguro;
    }
    
    /**
    * @generated
    */
    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    /**
     * Constructor por defecto.
     */
	public Casa() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
