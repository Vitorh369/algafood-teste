package com.algaworks.algafood.api.model.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class EnderecoInput {

	@NotBlank
	private String cep;
	
	@NotBlank
	private String logradouro;
	
	@NotBlank
	private String numero;
	
	
	private String complemento;
	
	@NotBlank
	private String bairro;

	@Valid
	@NonNull
	private CidadeIdInput cidade;
}
