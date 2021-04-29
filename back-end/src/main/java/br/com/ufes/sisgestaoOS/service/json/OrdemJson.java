package br.com.ufes.sisgestaoOS.service.json;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.model.Ordem;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrdemJson {
	int codOs;
	UsuarioJson solicitante;
	UsuarioJson encarregado;
	RequisitoJson requisito;
	StatusJson status;
	PrioridadeJson prioridade;
	EquipeJson equipe;
	Timestamp emissao;
	String descricao;
	BigDecimal esforco;
	Timestamp entrega;
	BigDecimal vlrEstimado;
	
	public Ordem getOrdem() {
		return new Ordem(
				this.codOs
				,this.solicitante.getUsuario()
				,this.encarregado.getUsuario()
				,this.requisito.getRequisito()
				,this.status.getStatus()
				,this.prioridade.getPrioridade()
				,this.equipe.getEquipe()
				,this.emissao
				,this.descricao
				,this.esforco
				,this.entrega
				,this.vlrEstimado
				);
	}
}
