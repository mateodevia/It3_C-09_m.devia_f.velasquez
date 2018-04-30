package auxiliary;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.Alojamiento;
import vos.Servicio;

public class UsoCliente {
	
	@JsonProperty(value = "idCliente")
	private Long idCliente;
	
	@JsonProperty(value = "diasContratados")
	private Integer diasContratados;
	
	@JsonProperty(value = "dineroPagado")
	private Double dineroPagado;
	
	@JsonProperty(value = "alojamiento")
	private Alojamiento alojamiento;

	public UsoCliente(@JsonProperty(value = "alojamiento") Alojamiento alojamiento, 
			@JsonProperty(value = "diasContratados") Integer diasContratados, 
			@JsonProperty(value = "dineroPagado") Double dineroPagado,
			@JsonProperty(value = "idCliente") Long idCliente) {
		this.diasContratados = diasContratados;
		this.dineroPagado = dineroPagado;
		this.alojamiento = alojamiento;
		this.idCliente = idCliente;
	}

	public Alojamiento getAlojamiento() {
		return alojamiento;
	}

	public void setAlojamiento(Alojamiento idAlojamiento) {
		this.alojamiento = idAlojamiento;
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

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	
}
