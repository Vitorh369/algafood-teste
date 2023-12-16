package com.algaworks.algafood.api.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.disassemblers.PedidoInputDisassembler;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.openapi.controller.PedidoControllerOpenApi;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PagebleTranslator;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidoSpecs;

@RestController
@RequestMapping(path = "/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private EmissaoPedidoService emissaoPedido;
    
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;
    
    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    
    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;
    
    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
    
    
    // aula 13.2
    //usando @JsonFilter("pedidoFilter") que anotamoas no PedidoModel
//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//        List<Pedido> pedidos = pedidoRepository.findAll();
//        List<PedidoResumoModel> pedidosModel = pedidoResumoModelAssembler.toCollectionModel(pedidos);
//        
//        MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
//        
//        // por padrão devolve toddospq ta anotado @RequestParam(required = false)
//        SimpleFilterProvider filterProvider = new  SimpleFilterProvider();
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//        
//        //SimpleBeanPropertyFilter.serializeAllExcept: filtra apenas "id", "valorTotal"
//        //filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAllExcept("id", "valorTotal"));
//        
//        // logica caso seja uma pesquisa dinamica
//        if(StringUtils.isNotBlank(campos)) {
//        	filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//        }
//        
//        pedidosWrapper.setFilters(filterProvider);
//        
//        return pedidosWrapper;
//    }
    
    
    
    @Override
    @GetMapping//aula 13.6 e 13.10
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, 
			@PageableDefault(size = 10) Pageable pageable) {
		Pageable pageableTraduzido= traduzirPageble(pageable);

		Page<Pedido> pedidosPage = pedidoRepository.findAll(
				PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);
		
		pedidosPage = new PageWrapper<>(pedidosPage, pageable);
		
		return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
	}

   
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    } 
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncotradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    // aula 13.11
    // converte Pageable para que a requisição feita por usuario seja retornado. exemplo:
    //se mudar o nosso model e tivemos que fazer a busca por exemplo como cliente.nome, que não intuitivo vamos fazer com q esse valor
    //seja nomeCliente. nomeCliente = cliente.nome como abaixo no map
    private Pageable traduzirPageble(Pageable pageable) {
    	
    	var mapeamento = Map.of(
				"codigo", "codigo",
				"subtotal", "subtotal",
				"taxaFrete", "taxaFrete",
				"valorTotal", "valorTotal",
				"dataCriacao", "dataCriacao",
				"restaurante.nome", "restaurante.nome",
				"restaurante.id", "restaurante.id",
				"cliente.id", "cliente.id",
				"cliente.nome", "cliente.nome"
			);
    	
    	return PagebleTranslator.translate(pageable, mapeamento);
    }
}           












