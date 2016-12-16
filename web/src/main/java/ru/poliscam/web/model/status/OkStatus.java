package ru.poliscam.web.model.status;

public class OkStatus {

	private Object data;
	private Long time;

	public OkStatus() {
	}

	public OkStatus(Object data, Long time) {
		this.data = data;
		this.time = time;
	}

	public String getStatus() {
		return "OK";
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
