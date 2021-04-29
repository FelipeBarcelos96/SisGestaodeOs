package br.com.ufes.sisgestaoOS.api.handlers.ordens;

import java.math.BigDecimal;
import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.service.json.EquipeJson;
import br.com.ufes.sisgestaoOS.service.json.PrioridadeJson;
import br.com.ufes.sisgestaoOS.service.json.RequisitoJson;
import br.com.ufes.sisgestaoOS.service.json.StatusJson;
import br.com.ufes.sisgestaoOS.service.json.UsuarioJson;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
class OrdemRegistrationRequest {
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
}
