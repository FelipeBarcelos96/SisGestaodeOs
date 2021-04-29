package br.com.ufes.sisgestaoOS.model;

import java.sql.Timestamp;

public class Requisito {
	private int codReq;
	private Usuario analista;
	private String titulo;
	private String descricao;
	private Timestamp prazo;
	
	public Requisito(int codReq, Usuario analista, String titulo, String descricao, Timestamp prazo) {
		this.codReq = codReq;
		this.analista = analista;
		this.titulo = titulo;
		this.descricao = descricao;
		this.prazo = prazo;
	}

	public Requisito(Usuario analista, String titulo, String descricao, Timestamp prazo) {
		this.analista = analista;
		this.titulo = titulo;
		this.descricao = descricao;
		this.prazo = prazo;
	}

	public int getCodReq() {
		return codReq;
	}

	public Usuario getAnalista() {
		return analista;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Timestamp getPrazo() {
		return prazo;
	}

	public void setCodReq(int codReq) {
		this.codReq = codReq;
	}

	public void setAnalista(Usuario analista) {
		this.analista = analista;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setPrazo(Timestamp prazo) {
		this.prazo = prazo;
	}
	
	

}
