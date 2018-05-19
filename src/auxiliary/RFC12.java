package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC12 {

	@JsonProperty("semana")
	private Integer semana;
	
	@JsonProperty("alojamientoMasOcupacion")
	private String alojamientoMasOcupacion;
	
	@JsonProperty("alojamientoMenosOcupacion")
	private String alojamientoMenosOcupacion;
	
	@JsonProperty("operadorMasSolicitado")
	private String operadorMasSolicitado;
	
	@JsonProperty("operadorMenosSolicitado")
	private String operadorMenosSolicitado;

	
	public RFC12(@JsonProperty("semana") Integer semana) {
		this.semana = semana;
	}

	public Integer getSemana() {
		return semana;
	}

	public void setSemana(Integer semana) {
		this.semana = semana;
	}

	public String getAlojamientoMasOcupacion() {
		return alojamientoMasOcupacion;
	}

	public void setAlojamientoMasOcupacion(String alojamientoMasOcupacion) {
		this.alojamientoMasOcupacion = alojamientoMasOcupacion;
	}

	public String getAlojamientoMenosOcupacion() {
		return alojamientoMenosOcupacion;
	}

	public void setAlojamientoMenosOcupacion(String alojamientoMenosOcupacion) {
		this.alojamientoMenosOcupacion = alojamientoMenosOcupacion;
	}

	public String getOperadorMasSolicitado() {
		return operadorMasSolicitado;
	}

	public void setOperadorMasSolicitado(String operadorMasSolicitado) {
		this.operadorMasSolicitado = operadorMasSolicitado;
	}

	public String getOperadorMenosSolicitado() {
		return operadorMenosSolicitado;
	}

	public void setOperadorMenosSolicitado(String operadorMenosSolicitado) {
		this.operadorMenosSolicitado = operadorMenosSolicitado;
	}
	
	
	
}
