package br.com.ufes.sisgestaoOS.service.json;

import br.com.ufes.sisgestaoOS.model.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatusJson {
	int codStatus;
    String status;
    
    public Status getStatus() {
    	return new Status(
    			this.codStatus
    			,this.status
    			);
    }
}
