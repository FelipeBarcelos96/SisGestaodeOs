package br.com.ufes.sisgestaoOS.service.json;

import java.sql.Timestamp;

import br.com.ufes.sisgestaoOS.model.Requisito;
import br.com.ufes.sisgestaoOS.model.Usuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RequisitoJson {
	int codReq;
	UsuarioJson analista;
	String titulo;
	String descricao;
	Timestamp prazo;
	
	public Requisito getRequisito() {
    	return new Requisito(
    			this.codReq
    			,this.analista.getUsuario()
    			,this.titulo
    			,this.descricao
    			,this.prazo			
    			);
    }
}
