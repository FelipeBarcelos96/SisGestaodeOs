package br.com.ufes.sisgestaoOS.service.json;

import br.com.ufes.sisgestaoOS.model.Usuario;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UsuarioJson {
	int id;
    EquipeJson equipe;
    String login;
    String password;
    Boolean ehAdm;
    Boolean ehGestor;
    Boolean ehDev;
    Boolean ehAnal;
    
    public Usuario getUsuario() {
    	return new Usuario(
    			this.id
    			,this.login
    			,this.password
    			,this.ehAdm
    			,this.ehGestor
    			,this.ehDev
    			,this.ehAnal
    			,this.equipe.getEquipe()    			
    			);
    }
}
