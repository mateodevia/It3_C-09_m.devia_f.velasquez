package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC10 {
	
	@JsonProperty("clasificacion")
	private String clasificacion;
	
	@JsonProperty("apariciones")
	private Integer apariciones;

	
	
	public RFC10(@JsonProperty("clasificacion")String clasificacion, @JsonProperty("apariciones")Integer apariciones) {
		super();
		this.clasificacion = clasificacion;
		this.apariciones = apariciones;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public Integer getApariciones() {
		return apariciones;
	}

	public void setApariciones(Integer apariciones) {
		this.apariciones = apariciones;
	}
	
	
	
}
