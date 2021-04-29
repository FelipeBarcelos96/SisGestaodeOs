package br.com.ufes.sisgestaoOS.api.handlers.status;

import lombok.Value;

@Value
class StatusUpdateRequest {
	int codStatus;
	String status;    
}