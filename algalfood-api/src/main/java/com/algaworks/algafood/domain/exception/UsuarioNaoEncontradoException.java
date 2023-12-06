package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncotradaException {

	
	private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(String mensagem) {
		super(mensagem);
	}

	public UsuarioNaoEncontradoException(Long usuarioId) {
		this(String.format("Não existe um cadastro de usúario com código %d", usuarioId));
	}
	
}
