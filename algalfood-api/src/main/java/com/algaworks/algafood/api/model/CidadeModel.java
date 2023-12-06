package com.algaworks.algafood.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
//aula 18.10
//@ApiModel(value ="Cidade", description = "representa uma cidade")
@Getter
@Setter
public class CidadeModel {

	//@ApiModelProperty(value = "ID da cidade", example = "1")
	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Uberlândia")
	private String nome;
	
	private EstadoModel estado;
}
