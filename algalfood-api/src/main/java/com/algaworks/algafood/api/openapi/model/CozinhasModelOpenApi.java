package com.algaworks.algafood.api.openapi.model;

import java.util.List;

import org.springframework.hateoas.Links;

import com.algaworks.algafood.api.model.CidadeModel;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//class para documentacao aula 18.21
@ApiModel("CozinhasModel")
@Setter
@Getter
public class CozinhasModelOpenApi {

private CidadeEmbeddedModelOpenApi _embedded;
	
	private Links _links;

	@ApiModel("CidadesembeddedModel")
	@Data
	public class CidadeEmbeddedModelOpenApi {
		
		private List<CidadeModel> cidades;
	}
}
