package br.com.ufes.sisgestaoOS.service.novo;

import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.model.Usuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NovoRequisito {	
	Usuario analista;
	String titulo;
	String descricao;
	Timestamp prazo;
}
