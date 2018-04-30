package vos;

import java.sql.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class FechasServicios {

	//------------------------------------------------------------------------------------------
    // Atributos
    //------------------------------------------------------------------------------------------
	
	@JsonProperty(value="fechaInicio")
    private Date fechaInicio;

	@JsonProperty(value="fechaFin")
    private Date fechaFin;
    
	@JsonProperty(value="servicios")
	private List<String> servicios;
    
	//------------------------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------------------------
	
	public FechasServicios(@JsonProperty(value="fechaInicio")Date fechaInicio, @JsonProperty(value="fechaFin")Date fechaFin, @JsonProperty(value="servicios")List<String> servicios) {

		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.servicios = servicios;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public List<String> getServicios() {
		return servicios;
	}

	public void setServicios(List<String> servicios) {
		this.servicios = servicios;
	}
	
	//------------------------------------------------------------------------------------------
    // Getters y Setters
    //------------------------------------------------------------------------------------------

}
