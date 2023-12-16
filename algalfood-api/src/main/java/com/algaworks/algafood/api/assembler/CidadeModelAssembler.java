package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.domain.model.Cidade;

//RepresentationModelAssemblerSupport aula 19.11
//modificando class para incluir links
@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeModel>{


	@Autowired
	private ModelMapper modelMapper;
	
	
	@Autowired
	private AlgaLinks algaLinks;
	
	public CidadeModelAssembler() {
		super(CidadeController.class, CidadeModel.class);

	}

	//aula 19.11
	@Override
	public CidadeModel toModel(Cidade cidade) {
		
		CidadeModel cozinhaModel = createModelWithId(cidade.getId(), cidade);
		
		 modelMapper.map(cidade, cozinhaModel);
		
		 cozinhaModel.add(algaLinks.linkToCozinhas("cozinhas"));

		return cozinhaModel;
	}
	
	@Override
	public CollectionModel<CidadeModel> toCollectionModel(Iterable<? extends Cidade> entities) {
	    return super.toCollectionModel(entities)
	            .add(algaLinks.linkToCidades());
	}
}
