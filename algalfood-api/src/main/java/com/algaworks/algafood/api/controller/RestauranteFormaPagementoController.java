package com.algaworks.algafood.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamentos" )
public class RestauranteFormaPagementoController implements RestauranteFormaPagamentoControllerOpenApi{

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;
	
	@Autowired
	private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante =  cadastroRestaurante.buscarOuFalhar(restauranteId);
		
		//aula 19.28, 19.29
		CollectionModel<FormaPagamentoModel> formasPagamentoModel 
		= formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamentos())
				.removeLinks()
				.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId))// link para desassociar a forma de pagamento quando listar
				.add(algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "assocciar"));// link para associar a forma de pagamento quando listar
		
		formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
			formaPagamentoModel.add(algaLinks.
					linkToRestauranteFormaPagamentoDesassociacao(restauranteId, formaPagamentoModel.getId(), "desassocciar"));
		});
		
		return formasPagamentoModel;
	}
	
	// desassociando a forma de pagamento com o restaurante
	@DeleteMapping(value = "/{formaPagamentoId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		
		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
		
		return ResponseEntity.noContent().build();
	}
	
	// asossicar a forma de pagamento com o restaurante
	@PutMapping(value = "/{formaPagamentoId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
			
		cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);
		
		return ResponseEntity.noContent().build();
	}

}




