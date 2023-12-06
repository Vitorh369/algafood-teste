package com.algaworks.algafood.api.model;
import java.math.BigDecimal;

import com.algaworks.algafood.api.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RestauranteModel {

	//@JsonView server para resumir oq precisamos
	@JsonView({RestauranteView.resumo.class, RestauranteView.apenasNome.class})
	private Long id;
	
	@JsonView({RestauranteView.resumo.class, RestauranteView.apenasNome.class})
	private String nome;
	
	@JsonView(RestauranteView.resumo.class)
	private BigDecimal taxaFrete;
	
	@JsonView(RestauranteView.resumo.class)
	private CozinhaModel cozinha;
	
	private Boolean ativo;
	private EnderecoModel endereco;
	private Boolean aberto;

}
