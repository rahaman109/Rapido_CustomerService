package com.alpha.customerservice.responsedto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseStructure<T> {

	@JsonProperty("statuscode")
	private int statusCode;
	private String message;
	private T data;

	public ResponseStructure(int statusCode, String message, T data) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.data = data;
	}

	public ResponseStructure() {
		super();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
