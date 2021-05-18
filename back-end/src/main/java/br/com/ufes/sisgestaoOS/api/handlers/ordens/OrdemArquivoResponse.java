package br.com.ufes.sisgestaoOS.api.handlers.ordens;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class OrdemArquivoResponse {
	String nome;
	String dados;
	String info;
}
