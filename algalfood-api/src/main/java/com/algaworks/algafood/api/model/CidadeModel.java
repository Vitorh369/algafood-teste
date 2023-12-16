package com.algaworks.algafood.api.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//nome para array de links
@Relation(collectionRelation = "cidades")
@Getter
@Setter
//RepresentationModel para urls. aula 19.7 Adicionando hypermedia na representação de recurso único com HAL
public class CidadeModel extends RepresentationModel<CidadeModel>{

	@ApiModelProperty(example = "1")
	private Long id;
	
	@ApiModelProperty(example = "Uberlândia")
	private String nome;
	
	@ApiModelProperty(example = "Minas Gerais")
	private EstadoModel estado;
}
