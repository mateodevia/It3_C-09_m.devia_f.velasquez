package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

import vos.Cliente;

public class RFC13 {

	private class TipoJustificacion{
		private final static int TODOS_MESES = 0;
		private final static int SIEMPRE_COSTOSO = 1;
		private final static int SIEMPRE_SUITE = 2;
	}
	
	
	@JsonProperty(value = "cliente")
	private  Cliente cliente;
	
	@JsonProperty(value = "justificacion")
	private String justificacion;
	
	public RFC13(@JsonProperty(value = "cliente") Cliente cliente, 
			@JsonProperty(value = "justificacion")Integer justificacion) {
		
		this.cliente = cliente;
		
		
		this.justificacion = determinarJustificacion(justificacion);
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}
	
	private String determinarJustificacion(int justificacion) {
		
		if(justificacion == TipoJustificacion.SIEMPRE_COSTOSO) {
			return "Siempre ha reservado alojamientos costosos";
		}
		else if(justificacion == TipoJustificacion.SIEMPRE_SUITE) {
			return "Siempre ha reservado suites";
		}
		else if(justificacion == TipoJustificacion.TODOS_MESES) {
			return "Ha reservado al menos una vez al mes";
		}
		
		else return "ERROR";
	}
}
