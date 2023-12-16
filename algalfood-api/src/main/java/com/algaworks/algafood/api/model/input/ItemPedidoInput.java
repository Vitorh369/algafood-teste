package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoInput {

	@ApiModelProperty(example = "1", required = true)
	@NotNull
	private Long produtoId;
	
	@ApiModelProperty(example = "2", required = true)
	@PositiveOrZero
	@NotNull
	private Integer quantidade;
	
	@ApiModelProperty(example = "Menos picante, por favo")
	private String observacao;
}
