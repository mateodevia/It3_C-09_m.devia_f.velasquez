package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

public class RFC1 {

	
	@JsonProperty(value = "proveedor")
	private Long proveedor;
	
	@JsonProperty(value = "dinero")
	private Double dinero;

	public Long getProveedor() {
		return proveedor;
	}

	public void setProveedor(Long proveedor) {
		this.proveedor = proveedor;
	}

	public Double getDinero() {
		return dinero;
	}

	public void setDinero(Double dinero) {
		this.dinero = dinero;
	}

	public RFC1(Long proveedor, Double dinero) {
		this.proveedor = proveedor;
		this.dinero = dinero;
	}
	
	
	
}
