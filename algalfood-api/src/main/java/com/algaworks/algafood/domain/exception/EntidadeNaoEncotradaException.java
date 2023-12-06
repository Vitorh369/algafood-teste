package com.algaworks.algafood.domain.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND) // retorna 404 não encotrado
public abstract class EntidadeNaoEncotradaException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncotradaException(String mensagem) {
		super(mensagem);
	}

}
