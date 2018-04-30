package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.Alojamiento;

public class UsoTipoCliente {

	
	@JsonProperty(value = "tipoCliente")
	private String tipoCliente;
	
	@JsonProperty(value = "diasContratados")
	private Integer diasContratados;
	
	@JsonProperty(value = "dineroPagado")
	private Double dineroPagado;
	
	@JsonProperty(value = "alojamiento")
	private Alojamiento alojamiento;
	
	

	public UsoTipoCliente(@JsonProperty(value = "tipoCliente" )String tipoCliente,
			@JsonProperty(value = "diasContratados")Integer diasContratados, 
			@JsonProperty(value = "dineroPagado")Double dineroPagado, 
			@JsonProperty(value = "alojamiento")Alojamiento alojamiento) {
		this.tipoCliente = tipoCliente;
		this.diasContratados = diasContratados;
		this.dineroPagado = dineroPagado;
		this.alojamiento = alojamiento;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Integer getDiasContratados() {
		return diasContratados;
	}

	public void setDiasContratados(Integer diasContratados) {
		this.diasContratados = diasContratados;
	}

	public Double getDineroPagado() {
		return dineroPagado;
	}

	public void setDineroPagado(Double dineroPagado) {
		this.dineroPagado = dineroPagado;
	}

	public Alojamiento getAlojamiento() {
		return alojamiento;
	}

	public void setAlojamiento(Alojamiento alojamiento) {
		this.alojamiento = alojamiento;
	}
	
	
	
}
