package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class ViviendaUniversitaria extends Operador {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="direccion")
    private String direccion;
    
    
    /**
    * @generated
    */
	@JsonProperty(value="habVivienda")
    private List<HabVivienda> habVivienda;
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------

	public ViviendaUniversitaria(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo,
			@JsonProperty(value="direccion")String direccion,
			@JsonProperty(value="habVivienda")List<HabVivienda> habVivienda) {
		
		super(id, nombre, tipo);
		this.direccion = direccion;
		this.habVivienda = habVivienda;
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
    public List<HabVivienda> getHabVivienda() {
        return this.habVivienda;
    }
    
    /**
    * @generated
    */
    public void setHabVivienda(List<HabVivienda> habVivienda) {
        this.habVivienda = habVivienda;
    }

    /**
     * Constructor por defecto
     */
	public ViviendaUniversitaria() {
	}
    
}
