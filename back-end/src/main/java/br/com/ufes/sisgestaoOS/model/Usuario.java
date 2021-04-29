package br.com.ufes.sisgestaoOS.model;


public class Usuario {
	private int id;
	private String nome;
	private String pass;
    private Boolean ehAdm;
    private Boolean ehGestor;
    private Boolean ehDev;
    private Boolean ehAnal;
    private Equipe equipe;
    
    
	public Usuario(int id, String nome, String pass, Boolean ehAdm, Boolean ehGestor, Boolean ehDev,
			Boolean ehAnal, Equipe equipe) {
		
		this.id = id;
		this.nome = nome;
		this.pass = pass;
		this.ehAdm = ehAdm;
		this.ehGestor = ehGestor;
		this.ehDev = ehDev;
		this.ehAnal = ehAnal;
		this.equipe = equipe;
	}
	

	public Usuario(String nome, String pass, Boolean ehAdm, Boolean ehGestor, Boolean ehDev, Boolean ehAnal, Equipe equipe) {
		
		this.nome = nome;
		this.pass = pass;
		this.ehAdm = ehAdm;
		this.ehGestor = ehGestor;
		this.ehDev = ehDev;
		this.ehAnal = ehAnal;
		this.equipe = equipe;
	}
	
	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getPass() {
		return pass;
	}

	public Boolean getEhAdm() {
		return ehAdm;
	}

	public Boolean getEhGestor() {
		return ehGestor;
	}

	public Boolean getEhDev() {
		return ehDev;
	}

	public Boolean getEhAnal() {
		return ehAnal;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setEhAdm(Boolean ehAdm) {
		this.ehAdm = ehAdm;
	}

	public void setEhGestor(Boolean ehGestor) {
		this.ehGestor = ehGestor;
	}

	public void setEhDev(Boolean ehDev) {
		this.ehDev = ehDev;
	}

	public void setEhAnal(Boolean ehAnal) {
		this.ehAnal = ehAnal;
	}

	public void setId(int id) {
		this.id = id;
	}


	public Equipe getEquipe() {
		return equipe;
	}


	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	} 
	
	
    
}
