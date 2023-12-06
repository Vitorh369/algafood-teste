package com.algaworks.algafood.domain.service;

import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
//aula 15.3
public interface EnvioEmailService {

	void enviar(Mensagem mensagem);
	
	@Getter
	@Builder
	class Mensagem {
		
		//@Singular troca o termo set para um singular 
		@Singular
		private Set<String> destinatarios;
		
		@NonNull
		private String assunto;
		
		@NonNull
		private String corpo;
		
		// como lombok nao reconhe variaveis que em portugues, passamos no argumento o singular
		@Singular("variavel")
		private Map<String, Object> variaveis;
		
	}
}
