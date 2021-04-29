package br.com.ufes.sisgestaoOS.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ordem {
	private int codOs;
	private Usuario solicitante;
	private Usuario encarregado;
	private Requisito requisito;
	private Status status;
	private Prioridade prioridade;
	private Equipe equipe;
	private Timestamp emissao;
	private String descricao;
	private BigDecimal esforco;
	private Timestamp entrega;
	private BigDecimal vlrEstimado;
	
	public Ordem(int codOs, Usuario solicitante, Usuario encarregado, Requisito requisito, Status status,
			Prioridade prioridade, Equipe equipe, Timestamp emissao, String descricao, BigDecimal esforco,
			Timestamp entrega, BigDecimal vlrEstimado) {
		this.codOs = codOs;
		this.solicitante = solicitante;
		this.encarregado = encarregado;
		this.requisito = requisito;
		this.status = status;
		this.prioridade = prioridade;
		this.equipe = equipe;
		this.emissao = emissao;
		this.descricao = descricao;
		this.esforco = esforco;
		this.entrega = entrega;
		this.vlrEstimado = vlrEstimado;
	}
	
	

	public Ordem(Usuario solicitante, Usuario encarregado, Requisito requisito, Status status, Prioridade prioridade,
			Equipe equipe, Timestamp emissao, String descricao, BigDecimal esforco, Timestamp entrega,
			BigDecimal vlrEstimado) {
		this.solicitante = solicitante;
		this.encarregado = encarregado;
		this.requisito = requisito;
		this.status = status;
		this.prioridade = prioridade;
		this.equipe = equipe;
		this.emissao = emissao;
		this.descricao = descricao;
		this.esforco = esforco;
		this.entrega = entrega;
		this.vlrEstimado = vlrEstimado;
	}



	public int getCodOs() {
		return codOs;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public Usuario getEncarregado() {
		return encarregado;
	}

	public Requisito getRequisito() {
		return requisito;
	}

	public Status getStatus() {
		return status;
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public Timestamp getEmissao() {
		return emissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public BigDecimal getEsforco() {
		return esforco;
	}

	public Timestamp getEntrega() {
		return entrega;
	}

	public BigDecimal getVlrEstimado() {
		return vlrEstimado;
	}

	public void setCodOs(int codOs) {
		this.codOs = codOs;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public void setEncarregado(Usuario encarregado) {
		this.encarregado = encarregado;
	}

	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public void setEmissao(Timestamp emissao) {
		this.emissao = emissao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEsforco(BigDecimal esforco) {
		this.esforco = esforco;
	}

	public void setEntrega(Timestamp entrega) {
		this.entrega = entrega;
	}

	public void setVlrEstimado(BigDecimal vlrEstimado) {
		this.vlrEstimado = vlrEstimado;
	}
	
}
