package com.algaworks.algafood.api.openapi.model;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

// class para documentacao para uso de Pageble.
@ApiModel("Pageble")
@Setter
@Getter
public class PageableModelOpenApi {

	@ApiModelProperty(example = "0", value = "número da página (começao em 0)")
	private int page;
	
	@ApiModelProperty(example = "10", value = "quantidade de elemetos por página")
	private int size;
	
	@ApiModelProperty(example = "nome, asc", value = "nome da propriedade para ordenação")
	private List<String> sort;
}
