package com.algaworks.algafood.api.model;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "restaurantes")
@Getter
@Setter
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

	@ApiModelProperty(example = "1")
	//@JsonView server para resumir oq precisamos
	//@JsonView({RestauranteView.resumo.class, RestauranteView.apenasNome.class})
	private Long id;
	
	@ApiModelProperty(example = "Thai Gourmet")
	//@JsonView({RestauranteView.resumo.class, RestauranteView.apenasNome.class})
	private String nome;
	
	@ApiModelProperty(example = "12.00")
	//@JsonView(RestauranteView.resumo.class)
	private BigDecimal taxaFrete;
	
	//@JsonView(RestauranteView.resumo.class)
	private CozinhaModel cozinha;
	
	private Boolean ativo;
	private EnderecoModel endereco;
	private Boolean aberto;

}
