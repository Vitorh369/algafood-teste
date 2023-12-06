package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncotradaException {

	
	private static final long serialVersionUID = 1L;
	
	public ProdutoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public ProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
		this(String.format("Não existe um cadastro de produto com código %d para restaurante de código %d", 
				produtoId, restauranteId));
	}

}
