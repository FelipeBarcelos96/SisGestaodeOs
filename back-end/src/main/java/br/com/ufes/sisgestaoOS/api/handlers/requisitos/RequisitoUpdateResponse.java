package br.com.ufes.sisgestaoOS.api.handlers.requisitos;

import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.service.json.UsuarioJson;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
class RequisitoUpdateResponse {
	int codReq;
	UsuarioJson analista;
	String Titulo;
	String Descricao;
	Timestamp prazo;
}
