package com.algaworks.algafood.domain.exception;

public  class EstadoNaoEncotradaException extends EntidadeNaoEncotradaException {

	private static final long serialVersionUID = 1L;

	public EstadoNaoEncotradaException(String mensagem) {
		super(mensagem);
	}

	public EstadoNaoEncotradaException(Long estadoId) {
		this(String.format("Não existe um cadastro de estado com codigo %d", estadoId));
	}
}
