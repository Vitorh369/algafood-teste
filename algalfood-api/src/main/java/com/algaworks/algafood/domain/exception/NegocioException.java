package com.algaworks.algafood.domain.exception;

//@ResponseStatus(HttpStatus.BAD_REQUEST) //devolve 400
public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	public NegocioException(String mensagem, Throwable causa) { // Throwable CLASS PAI DE TODAS AS EXECOES. ADIONAMOS PARA PODER ACHAR A CAUSA DO ERRO, APARECERAR NO STATUS
		super(mensagem, causa);
	}

}
