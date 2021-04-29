package br.com.ufes.sisgestaoOS.service.json;

import br.com.ufes.sisgestaoOS.model.Prioridade;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PrioridadeJson {
	int id;
	String nome;
	
	public Prioridade getPrioridade() {
		return new Prioridade(
				this.id
				,this.nome
				);
	}
}
