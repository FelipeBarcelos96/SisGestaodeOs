package br.com.ufes.sisgestaoOS.service.json;

import br.com.ufes.sisgestaoOS.model.Equipe;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EquipeJson {
	int codEquipe;
    String sigla;
    
    public  Equipe getEquipe() {
    	return new Equipe(
    			this.codEquipe
    			,this.sigla
    			);
    		
    	}
    
}
