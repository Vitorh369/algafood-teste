package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncotradaException {

	private static final long serialVersionUID = 1L;
	
	public GrupoNaoEncontradoException(String mensagem) {
		super(mensagem);
		
	}
	
	public GrupoNaoEncontradoException(Long estadoId) {
		this(String.format("Não existe um cadastro de grupo com codigo%d", estadoId));
	}

}
