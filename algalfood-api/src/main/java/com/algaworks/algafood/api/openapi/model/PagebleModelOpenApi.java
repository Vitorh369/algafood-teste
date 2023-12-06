package com.algaworks.algafood.api.openapi.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//class generica para documentacao
@Getter
@Setter
public class PagebleModelOpenApi<T> {

	private List<T> content;
	
	@ApiModelProperty(example = "10", value = "Quantidade de registro por página")
	private Long size;
	
	@ApiModelProperty(example = "50", value = "Total de regirstro")
	private Long totalElements;
	
	@ApiModelProperty(example = "5", value = "Total de páginas")
	private Long totalPages;
	
	@ApiModelProperty(example = "10", value = "Número da página (Começa em 0)")
	private Long number;
}
