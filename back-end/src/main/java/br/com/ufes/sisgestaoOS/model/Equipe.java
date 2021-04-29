package br.com.ufes.sisgestaoOS.model;


public class Equipe {
	private int codEquipe;
	private String sigla;	
	
	public Equipe(String sigla) {
		this.sigla = sigla;
	}

	public Equipe(int codEquipe, String sigla) {
		this.codEquipe = codEquipe;
		this.sigla = sigla;
	}

	public int getCodEquipe() {
		return codEquipe;
	}

	public String getSigla() {
		return sigla;
	}

	public void setCodEquipe(int codEquipe) {
		this.codEquipe = codEquipe;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
			
	
}
