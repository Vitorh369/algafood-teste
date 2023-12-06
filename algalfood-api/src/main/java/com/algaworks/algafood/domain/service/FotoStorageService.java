package com.algaworks.algafood.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

//serviço de armazenagem de foto
//aula 14.8, 14.9, 14.10
public interface FotoStorageService {
	
	FotoRecuperada recuperar(String nomeArquivo);
	
	void remover(String fotoArquivo);
	
	void armazenar(NovaFoto novaFoto);
	
	//metodo para substituir arquivo antigo por um novo arquivo
	default void substituir(String nomeArquivoAntigo, NovaFoto foto) {
		this.armazenar(foto);
		
		if(nomeArquivoAntigo != null) {
			this.remover(nomeArquivoAntigo);
		}
	};
	
	default String gerarNomeArquivo(String nomeOriginal) {
		return UUID.randomUUID().toString() + "_" + nomeOriginal;
 	}
	
	@Getter 
	@Builder
	class NovaFoto{
		
		private String nomeArquivo;
		private InputStream inputStream;
		private String contentType;
	}
	
	@Getter 
	@Builder
	class FotoRecuperada{
		private InputStream inputStream;
		private String url;
		
		public boolean temUrl() {
			return url != null;
		}
		
		public boolean temInputStream(){
			return inputStream != null;
		}
		
	}
}








