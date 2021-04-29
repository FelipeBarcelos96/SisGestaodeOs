package br.com.ufes.sisgestaoOS.model;


public class Status {
	private int codStatus;
	private String status;
	
	public Status(int codStatus, String status) {
		this.codStatus = codStatus;
		this.status = status;
	}

	public Status(String status) {
		this.status = status;
	}

	public int getCodStatus() {
		return codStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setCodStatus(int codStatus) {
		this.codStatus = codStatus;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
