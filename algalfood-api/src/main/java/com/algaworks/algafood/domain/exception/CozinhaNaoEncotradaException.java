package com.algaworks.algafood.domain.exception;

public  class CozinhaNaoEncotradaException extends EntidadeNaoEncotradaException {

	private static final long serialVersionUID = 1L;

	public CozinhaNaoEncotradaException(String mensagem) {
		super(mensagem);
	}

	public CozinhaNaoEncotradaException(Long cozinhaId) {
		this(String.format("Não existe cadasto de cozinha com codigo %d", cozinhaId));
	}
}
