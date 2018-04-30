package auxiliary;

import org.codehaus.jackson.annotate.JsonProperty;

public class Log {

	@JsonProperty(value = "msg")
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Log(@JsonProperty(value = "msg") String msg) {
		this.msg = msg;
	}
	
	
	
}
