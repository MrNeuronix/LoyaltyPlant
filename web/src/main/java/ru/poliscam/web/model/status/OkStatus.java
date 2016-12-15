package ru.poliscam.web.model.status;

public class OkStatus {

	private Object result;
	private Long time;

	public OkStatus() {
	}

	public OkStatus(Object text, Long time) {
		this.result = text;
		this.time = time;
	}

	public String getStatus() {
		return "OK";
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object text) {
		this.result = text;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
