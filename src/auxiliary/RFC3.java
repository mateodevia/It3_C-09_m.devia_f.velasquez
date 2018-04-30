package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC3 {

	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty(value = "indiceOcupacion")
	private Double indiceOcupacion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getIndiceOcupacion() {
		return indiceOcupacion;
	}

	public void setIndiceOcupacion(Double indiceOcupacion) {
		this.indiceOcupacion = indiceOcupacion;
	}

	public RFC3(@JsonProperty(value = "id") Long id,@JsonProperty(value = "indiceOcupacion") Double indiceOcupacion) {
		super();
		this.id = id;
		this.indiceOcupacion = indiceOcupacion;
	}
	
	
	
}
