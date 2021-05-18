package br.com.ufes.sisgestaoOS.api.handlers.ordens;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class OrdemArquivoRequest {
	int codOS;
	String nome;
	String dados;
	String info;
}
