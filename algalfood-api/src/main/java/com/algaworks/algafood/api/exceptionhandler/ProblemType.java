package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

	//EM PARENTESES É O ARGUMETNO DO CONSTRUTOR
	RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrada", "recurso nao encontrada"),
	
	ENTIDADE_EM_USO("/entidade-em-uso", "entidade em uso"),
	
	ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),   
	
	MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreesivel", "Mensagem incopreesível"),
	
	PARAMETRO_INVALIDO("/parametro-invalido", "Parametro invalido"),
	
	ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema"),
	
	DADOS_INVALIDOS("/dados-invalidos", "Dados invalidos");
	
	
	
	private String title;
	private String uri;
	
	private ProblemType(String path, String title) {
		this.uri = "https://algafood.com.br" + path;
		this.title = title;
	}
	
}
