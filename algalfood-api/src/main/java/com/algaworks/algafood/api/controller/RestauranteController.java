package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteApenasNomeModelAssembler;
import com.algaworks.algafood.api.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.disassemblers.RestauranteInputDisassembler;
import com.algaworks.algafood.api.model.RestauranteApenasNomeModel;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteControllerOpenApi;
import com.algaworks.algafood.api.openapi.model.RestauranteBasicoModelOpenApi;
import com.algaworks.algafood.domain.exception.CidadeNaoEncotradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncotradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//@CrossOrigin autorizacao de cors. aula 16.4
//@CrossOrigin
@RestController
@RequestMapping("restaurantes")
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

//	@Autowired
//	private SmartValidator validator;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;

	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;
	
	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler; 

	
	//aula 13.1	
//	//o metodo listarResumido() é um metodo onde devolvemos aulguns objetos pata diferenciar do metodo listar,
//	//passamos @GetMapping(params = "projecao=resumo")
//	//@JsonView para devolver uma representação resumida
	@ApiOperation(value = "Lista restaurante", response = RestauranteBasicoModelOpenApi.class)
	@ApiImplicitParams(
			@ApiImplicitParam(value = "Nome da projeção de pedidos", allowableValues="apenas-nome", 
			name="projecao", paramType = "query", type = "string"))
//	@JsonView(RestauranteView.Resumo.class)
	@Override
    @GetMapping
    public CollectionModel<RestauranteBasicoModel> listar() {
        return restauranteBasicoModelAssembler
                .toCollectionModel(restauranteRepository.findAll());
    }
	
	  @Override
//		@JsonView(RestauranteView.ApenasNome.class)
	    @GetMapping(params = "projecao=apenas-nome")
	    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
	        return restauranteApenasNomeModelAssembler
	                .toCollectionModel(restauranteRepository.findAll());
	    }
	
	
	//aula 13.1	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//		
//		List<Restaurante>  restaurantes = restauranteRepository.findAll();
//		List<RestauranteModel> restaurantesModel = restauranteModelAssembler.toCollectionModel(restaurantes);
//		
//		MappingJacksonValue restauranteWrapper = new MappingJacksonValue(restaurantesModel);
//		
//		// o padrão ja chama resumido quando não passamos parametros
//		restauranteWrapper.setSerializationView(RestauranteView.resumo.class);
//		
//		//passando parametros é feito a validacao
//		if("apenas-nome".equals(projecao)) {
//			restauranteWrapper.setSerializationView(RestauranteView.apenasNome.class);
//		} else if("completo".equals(projecao)) {
//			restauranteWrapper.setSerializationView(null);
//		}
//		
//		return restauranteWrapper;
//	}
	
	
//aula 13.1	codigo a cima é dinamico para busca
//	@GetMapping
//	public List<RestauranteModel> listar() {
//		return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
//	}
//	
//	
//	//o metodo listarResumido() é um metodo onde devolvemos aulguns objetos pata diferenciar do metodo listar,
//	//passamos @GetMapping(params = "projecao=resumo")
//	//@JsonView para devolver uma representação resumida
//	@JsonView(RestauranteView.resumo.class)
//	@GetMapping(params = "projecao=resumo")
//	public List<RestauranteModel> listarResumido() {
//		return listar();
//	}
//	

	@GetMapping(value ="{restauranteId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RestauranteModel buscarId(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		return restauranteModelAssembler.toModel(restaurante);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try {
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (RestauranteNaoEncotradoException | CidadeNaoEncotradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {

		try {
			// Restaurante restaurante =
			// restauranteInputDisassembler.toDomainObject(restauranteInput);

			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(id);

			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

			// BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
			// "formasPagamentos", "endereco", "dataCadastro", "produtos"); // o id,
			// formasPgamentos e endereco, dataCadastro não vai ser atualizado

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (RestauranteNaoEncotradoException | CidadeNaoEncotradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}

	}

	@PutMapping(value="/{restauranteId}/ativo", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void>  ativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.ativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{restauranteId}/ativo", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public  ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
		cadastroRestaurante.inativar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	// ativar resurantes em massa
	@PutMapping(value = "/ativacoes", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.ativar(restaurantesIds);
		} catch (RestauranteNaoEncotradoException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	// inativar restaurante em massa
	@DeleteMapping(value = "/ativacoes", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativartMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.inativar(restaurantesIds);
		} catch (RestauranteNaoEncotradoException e) {
			throw new NegocioException(e.getMessage());
		}

	}

	@PutMapping(value = "/{restauranteId}/abertura", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {
		cadastroRestaurante.abrir(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "/{restauranteId}/fechamento", produces = {})
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {
		cadastroRestaurante.fechar(restauranteId);
		
		return ResponseEntity.noContent().build();
	}

}

// METODO PARA ATUALIZAR POR PARTES
//	@PatchMapping("/{restauranteId}")
//	public RestauranteModel atualizarParcial(@PathVariable Long  restauranteId, 
//			@RequestBody Map<String, Object> campos, HttpServletRequest request){ // String É CHAVE e O Object é o valor
//		
//		
//		Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);
//			
//		merge(campos, restauranteAtual, request);
//		
//		Validate(restauranteAtual, "restaurate");
//		
//		return atualizar(restauranteId, restauranteAtual);
//	}
//
//
//	//aula 9.19
//	private void Validate(Restaurante restaurante, String objectName) {
//		
//		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);
//		validator.validate(restaurante, bindingResult);
//		
//		if(bindingResult.hasErrors()) {
//			throw new ValidacaoException(bindingResult);
//		}
//		
//	}
//
//	private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino, HttpServletRequest request) {
//			
//		ServletServerHttpRequest servletHttpRequest = new ServletServerHttpRequest(request);
//		
//		try {
//			ObjectMapper objectMapper = new ObjectMapper(); // CONVERTE JANSON EM OBJETO JAVA, E VICE-VERSO
//			
//			// CONFIGUNRANDO PARA LANÇAMENTO DE EXECESSAO! ESSA CONFIGURAÇAO É PQ QUANDO ATULIZAMOS A EXECESSAO NÃO É LANCADA E PARA ISSO ADIONAMOS ESSE DUAS CONFIGUTANÇÃO 
//			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
//			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
//			
//			Restaurante restauranteOrigem =  objectMapper.convertValue(dadosOrigem, Restaurante.class);// CRIA UMA INSTACIA DE Restaurante COM BASE AO map
//			
//			dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
//				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);//Field UMA CLASS UTILITARIA! A VARIAVEL field VAI REPRESENTA UM ATRIBUTO DA CLASS Restaurante! VAI FAZER DINAMICAMENTE EM TEMPO DE COMPILAÇÃO. EX: SE PRECISARMOS SO DO ATRIBUTO nome, DA CLASS Restaurante, VAI TRAZER A APENAS O NOME EM TEMPO DE COMPILAÇÃO
//				field.setAccessible(true);// COMO OS ATRIBUTOS SÃO PRIVADOS, NOS PERMITIMOS SER ACESSADO
//				
//				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);// BUSCANDO VALOR DO CAMPO
//				
//	//			System.out.println(nomePropriedade + " = "  + valorPropriedade + " = "  + novoValor);
//				
//				ReflectionUtils.setField(field, restauranteDestino, novoValor); // ESTOU ATRIBUINDO NO CAMPO(field), NO Object ResutanteDestino, PRA SETA novoValor
//		});
//	}catch(IllegalArgumentException e) {
//		Throwable rootCause = ExceptionUtils.getRootCause(e);
//		throw new HttpMessageNotReadableException(e.getMessage(), rootCause, servletHttpRequest);
//	}
//}
