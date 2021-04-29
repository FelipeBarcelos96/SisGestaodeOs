package br.com.ufes.sisgestaoOS.api.handlers.prioridades;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class PrioridadesRegistrationResponse {
	int id;
	String nome;
}
