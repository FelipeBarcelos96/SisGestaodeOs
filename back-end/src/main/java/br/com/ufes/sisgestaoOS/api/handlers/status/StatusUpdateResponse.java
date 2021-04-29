package br.com.ufes.sisgestaoOS.api.handlers.status;

import lombok.Value;

@Value
class StatusUpdateResponse {
	int codStatus;
	String status;    
}