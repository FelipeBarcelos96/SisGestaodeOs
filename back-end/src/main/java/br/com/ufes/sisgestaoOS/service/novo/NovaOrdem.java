package br.com.ufes.sisgestaoOS.service.novo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.model.Equipe;
import br.com.ufes.sisgestaoOS.model.Prioridade;
import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.model.Status;
import br.com.ufes.sisgestaoOS.model.Usuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NovaOrdem {
	Usuario solicitante;
	Usuario encarregado;
	Requisito requisito;
	Status status;
	Prioridade prioridade;
	Equipe equipe;
	Timestamp emissao;
	String descricao;
	BigDecimal esforco;
	Timestamp entrega;
	BigDecimal vlrEstimado;
}
