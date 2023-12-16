package com.algaworks.algafood.api.controller;

//aula 19.8 adicionando a importacao estatica 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.disassemblers.CidadeInputDisassembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.api.openapi.controller.CIidadeControllerOpenApi;
import com.algaworks.algafood.domain.exception.EstadoNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.ApiParam;

// aula 18.7 @Api. vinculadando tag cidade com a documentacao   

@RestController
@RequestMapping("cidades")
public class CidadeController implements CIidadeControllerOpenApi{

	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler; 
	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)	
	public CollectionModel<CidadeModel> listar() {
	    List<Cidade> todasCidades = cidadeRepository.findAll();
	   
	    return cidadeModelAssembler.toCollectionModel(todasCidades);
	
	}

	@GetMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CidadeModel buscar(@PathVariable Long cidadeId) {
	    Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
	    
	   return cidadeModelAssembler.toModel(cidade);
	}

	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
	    try {
	        Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);
	        
	        cidade = cadastroCidade.salvar(cidade);
	        
	        CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

	      //19.2. Adicionando a URI do recurso criado no header da resposta
	        ResourceUriHelper.addUriResponseHeader(cidadeModel.getId());
	        
	        return cidadeModel;
	    } catch (EstadoNaoEncotradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	
	@PutMapping(path = "/{cidadeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CidadeModel atualizar(@PathVariable Long cidadeId,
			@ApiParam(name ="corpo", value ="representação de uma cidade com os novos dados")
	        @RequestBody @Valid CidadeInput cidadeInput) {
	    try {
	        Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);
	        
	        cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
	        
	        cidadeAtual = cadastroCidade.salvar(cidadeAtual);
	        
	        return cidadeModelAssembler.toModel(cidadeAtual);
	    } catch (EstadoNaoEncotradaException e) {
	        throw new NegocioException(e.getMessage(), e);
	    }
	}

	@DeleteMapping(path = "/{cidadeId}", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT) // devolve 204
	public void delete(@ApiParam(value ="ID de uma cidade")@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}



}
