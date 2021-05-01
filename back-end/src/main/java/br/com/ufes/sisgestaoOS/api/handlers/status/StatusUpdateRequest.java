package br.com.ufes.sisgestaoOS.api.handlers.status;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
class StatusUpdateRequest {
	int codStatus;
	String status;    
}