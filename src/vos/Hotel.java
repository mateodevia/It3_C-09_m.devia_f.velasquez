package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Hotel extends Operador {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="direccion")
    private  String direccion;
    
    /**
    * @generated
    */
	@JsonProperty(value="registroCamara")
    private String registroCamara;
    
    /**
    * @generated
    */
	@JsonProperty(value="registroSuper")
    private String registroSuper;
    
    
    /**
    * @generated
    */
	@JsonProperty(value="habitaciones")
    private List<HabHotel> habitaciones;
    
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
	public Hotel(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="direccion")String direccion,
			@JsonProperty(value="registroCamara")String registroCamara, @JsonProperty(value="registroSuper")String registroSuper, @JsonProperty(value="habitaciones")List<HabHotel> habitaciones) {
		
		super(id, nombre, tipo);
		this.direccion = direccion;
		this.registroCamara = registroCamara;
		this.registroSuper = registroSuper;
		this.habitaciones = habitaciones;
	}
	
    //------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public String getDireccion() {
        return this.direccion;
    }
    
	/**
    * @generated
    */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
    /**
    * @generated
    */
    public String getRegistroCamara() {
        return this.registroCamara;
    }
    
    /**
    * @generated
    */
    public void setRegistroCamara(String registroCamara) {
        this.registroCamara = registroCamara;
    }
    
    
    /**
    * @generated
    */
    public String getRegistroSuper() {
        return this.registroSuper;
    }
    
    /**
    * @generated
    */
    public void setRegistroSuper(String registroSuper) {
        this.registroSuper = registroSuper;
    }
    
    /**
    * @generated
    */
    public List<HabHotel> getHabitaciones() {
        return this.habitaciones;
    }
    
    /**
    * @generated
    */
    public void setHabitaciones(List<HabHotel> habitaciones) {
        this.habitaciones = habitaciones;
    }
    
    /**
     * Constructor por defecto.
     */
	public Hotel() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
