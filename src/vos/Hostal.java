package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Hostal extends Operador {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="horarioApertura")
    private String horarioApertura;
    
    /**
    * @generated
    */
	@JsonProperty(value="horarioCierre")
    private String horarioCierre;
    
    /**
    * @generated
    */
	@JsonProperty(value="direccion")
    private String direccion;
    
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
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
    public Hostal(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo,
    		@JsonProperty(value="horarioApertura")String horarioApertura,
    		@JsonProperty(value="horarioCierre")String horarioCierre, @JsonProperty(value="direccion")String direccion, @JsonProperty(value="registroCamara")String registroCamara, @JsonProperty(value="registroSuper")String registroSuper,
    		@JsonProperty(value="habHostal")List<HabHostal> habHostal) {
		
    	super(id, nombre, tipo);
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.direccion = direccion;
		this.registroCamara = registroCamara;
		this.registroSuper = registroSuper;
		this.habHostal = habHostal;
	}
    
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------
    
    /**
    * @generated
    */
    private List<HabHostal> habHostal;
    
    /**
    * @generated
    */
    public String getHorarioApertura() {
        return this.horarioApertura;
    }
    
    /**
    * @generated
    */
    public void setHorarioApertura(String horarioApertura) {
        this.horarioApertura = horarioApertura;
    }
    
    
    /**
    * @generated
    */
    public String getHorarioCierre() {
        return this.horarioCierre;
    }
    
    /**
    * @generated
    */
    public void setHorarioCierre(String horaripCierre) {
        this.horarioCierre = horaripCierre;
    }
    
    
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
    public List<HabHostal> getHabHostal() {
        return this.habHostal;
    }
    
    /**
    * @generated
    */
    public void setHabHostal(List<HabHostal> habHostal) {
        this.habHostal = habHostal;
    }

    /**
     * Constructor por defecto.
     */
	public Hostal() {
		super();
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
