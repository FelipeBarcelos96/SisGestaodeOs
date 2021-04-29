package br.com.ufes.sisgestaoOS.model;

public class Prioridade {
	private int id;
	private String nome;
	
	public Prioridade(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Prioridade(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
