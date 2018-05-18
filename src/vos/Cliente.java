package vos;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
* @generated
*/
public class Cliente {
    
	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
    /**
    * @generated
    */
	@JsonProperty(value="carnetUniandes")
    private Long carnetUniandes;
    
    /**
    * @generated
    */
	@JsonProperty(value="nombre")
    private String nombre;
    
    /**
    * @generated
    */
	@JsonProperty(value="apellido")
    private String apellido;
    
    /**
    * @generated
    * tipoDocumento IN TipoDocumento.*
    */
	@JsonProperty(value="tipoDocumento")
    private String tipoDocumento;
    
    /**
    * @generated
    */
	@JsonProperty(value="numDocumento")
    private Long numDocumento;
    
    
    /**
    * @generated
    */
	@JsonProperty(value="reservas")
    private List<Reserva> reservas;
	
	@JsonProperty(value = "tipoCliente")
	private String tipoCliente;
	
	@JsonProperty(value = "fechaCreacion")
	private Date fechaCreacion;
    
    //------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
    
	public Cliente(@JsonProperty(value="carnetUniandes")Long carnetUniandes, @JsonProperty(value="nombre")String nombre, @JsonProperty(value="apellido")String apellido, @JsonProperty(value="tipoDocumento")String tipoDocumento, @JsonProperty(value="numDocumento")Long numDocumento,
			@JsonProperty(value="reservas")List<Reserva> reservas, @JsonProperty(value = "tipoCliente")String tipoCliente,
			@JsonProperty(value="fechaCreacion") Date fechaCreacion) {
		
		super();
		this.carnetUniandes = carnetUniandes;
		this.nombre = nombre;
		this.apellido = apellido;
		this.tipoDocumento = tipoDocumento;
		this.numDocumento = numDocumento;
		this.reservas = reservas;
		this.tipoCliente = tipoCliente;
		this.fechaCreacion = fechaCreacion;
	}
	
    //------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

    /**
    * @generated
    */
    public Long getCarnetUniandes() {
        return this.carnetUniandes;
    }
    
	/**
    * @generated
    */
    public void setCarnetUniandes(Long carnetUniandes) {
        this.carnetUniandes = carnetUniandes;
    }
    
    
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
    public String getApellido() {
        return this.apellido;
    }
    
    /**
    * @generated
    */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    
    /**
    * @generated
    */
    public String getTipoDocumento() {
        return this.tipoDocumento;
    }
    
    /**
    * @generated
    */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    
    /**
    * @generated
    */
    public Long getNumDocumento() {
        return this.numDocumento;
    }
    
    /**
    * @generated
    */
    public void setNumDocumento(Long numDocumento) {
        this.numDocumento = numDocumento;
    }
    
    
    
    /**
    * @generated
    */
    public List<Reserva> getReservas() {
        return this.reservas;
    }
    
    /**
    * @generated
    */
    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
    
    public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
     * Constructor por defecto.
     */
	public Cliente() {
		
	}
    
	//------------------------------------------------------------------------------------------
    // Metodos
    //------------------------------------------------------------------------------------------
    
}
