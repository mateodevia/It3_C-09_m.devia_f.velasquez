package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Operador {
	
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
    
	@JsonProperty(value="id")
	private Long id;
	
    /**
    * @generated
    */
	@JsonProperty(value="nombre")
    private String nombre;
    
    /**
    * @generated
    */
	@JsonProperty(value="tipo")
    private String tipo;
        
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public Operador(@JsonProperty(value="id")Long id, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo) {
		
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public String getNombre() {
        return this.nombre;
    }

	/**
    * @generated
    */
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    
    public Long getId() {
		return id;
	}
    
    public void setId(Long id) {
		this.id = id;
	}

    /**
     * Constructor por defecto
     */
	public Operador() {
		
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
