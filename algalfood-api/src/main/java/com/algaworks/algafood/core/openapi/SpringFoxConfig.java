package com.algaworks.algafood.core.openapi;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.CozinhaModel;
import com.algaworks.algafood.api.model.EstadoModel;
import com.algaworks.algafood.api.model.FormaPagamentoModel;
import com.algaworks.algafood.api.model.GrupoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.PermissaoModel;
import com.algaworks.algafood.api.model.ProdutoModel;
import com.algaworks.algafood.api.model.RestauranteBasicoModel;
import com.algaworks.algafood.api.model.UsuarioModel;
import com.algaworks.algafood.api.openapi.model.CidadesModelOpenApi;
import com.algaworks.algafood.api.openapi.model.CozinhasModelOpenApi;
import com.algaworks.algafood.api.openapi.model.EstadosModelOpenApi;
import com.algaworks.algafood.api.openapi.model.FormasPagamentoModelOpenApi;
import com.algaworks.algafood.api.openapi.model.GruposModelOpenApi;
import com.algaworks.algafood.api.openapi.model.LinksModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PageableModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PedidosResumoModelOpenApi;
import com.algaworks.algafood.api.openapi.model.PermissoesModelOpenApi;
import com.algaworks.algafood.api.openapi.model.ProdutosModelOpenApi;
import com.algaworks.algafood.api.openapi.model.RestaurantesBasicoModelOpenApi;
import com.algaworks.algafood.api.openapi.model.UsuariosModelOpenApi;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RepresentationBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.plugins.Docket;

//aula 18.3
// class de configuracao para documentacao do projeto
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)// importando class uma configuracao para essa class de configuracao
public class SpringFoxConfig {

	@Bean
	public Docket apiDocket() {
		
		var typeResolver = new TypeResolver();
		
		return new Docket(DocumentationType.OAS_30)
				.select().
					apis(RequestHandlerSelectors.basePackage("com.algaworks.algafood.api"))// se leciona a partir qual pacote scanear o projeto
					.paths(PathSelectors.any())
					//.paths(PathSelectors.ant("/restaurantes/*"))
					.build()
					.useDefaultResponseMessages(false)// removendo decricao de erro padro da documentacao
					.globalResponses(HttpMethod.GET,globalGetResponseMessages())
					.globalResponses(HttpMethod.POST,globalPostResponseMessages())
					.globalResponses(HttpMethod.PUT,globalPutResponseMessages())
					.globalResponses(HttpMethod.DELETE,globalDeleteResponseMessages())
//					.globalRequestParameters(Collections.singletonList(//18.25. Descrevendo parâmetros globais em operações
//				            new RequestParameterBuilder()
//				                    .name("campos")
//				                    .description("Nomes das propriedades para filtrar na resposta, separados por vírgula")
//				                    .in(ParameterType.QUERY)
//				                    .required(true)
//				                    .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
//				                    .build())
//				    )
					.additionalModels(typeResolver.resolve(Problem.class))// adcionando a class Problem na documentacao aula 18.14
					.ignoredParameterTypes(ServletWebRequest.class,
		                    URL.class, URI.class, URLStreamHandler.class, Resource.class,
		                    File.class, InputStream.class) //ignorando parametros para nao pararecer na documentacao
					.directModelSubstitute(Pageable.class, PageableModelOpenApi.class)// aula 18.20
					.directModelSubstitute(Links.class, LinksModelOpenApi.class)
					
					.alternateTypeRules(AlternateTypeRules.newRule(
							typeResolver.resolve(Page.class,CozinhaModel.class), CozinhasModelOpenApi.class)) // aula 18.21
					
					.alternateTypeRules(AlternateTypeRules.newRule(
		                    typeResolver.resolve(PagedModel.class, CidadeModel.class),
		                    CidadesModelOpenApi.class))
					
					.alternateTypeRules(AlternateTypeRules.newRule(
					        typeResolver.resolve(CollectionModel.class, EstadoModel.class),
					        EstadosModelOpenApi.class))
					
					.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(CollectionModel.class, FormaPagamentoModel.class),
						    FormasPagamentoModelOpenApi.class)) 
					
					.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(CollectionModel.class, GrupoModel.class),
						    GruposModelOpenApi.class))

					.alternateTypeRules(AlternateTypeRules.newRule(
						        typeResolver.resolve(CollectionModel.class, PermissaoModel.class),
						        PermissoesModelOpenApi.class))
					
					.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(PagedModel.class, PedidoResumoModel.class),
						    PedidosResumoModelOpenApi.class))
					
					.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(CollectionModel.class, ProdutoModel.class),
						    ProdutosModelOpenApi.class))
					
					.alternateTypeRules(AlternateTypeRules.newRule(
						    typeResolver.resolve(CollectionModel.class, RestauranteBasicoModel.class),
						    RestaurantesBasicoModelOpenApi.class))

					.alternateTypeRules(AlternateTypeRules.newRule(
						        typeResolver.resolve(CollectionModel.class, UsuarioModel.class),
						     UsuariosModelOpenApi.class))
					
				.apiInfo(apiInfo())
				.tags(new Tag("Cidades", "Gerencia as cidades"),
						new Tag("Grupos", "Gerencia os grupos de usuários"),
						new Tag("Cozinhas", "Gerencia as cozinhas"),
						new Tag("Formas de pagamento", "Gerencia as formas de pagamento"),
						new Tag("Pedidos", "Gerencia os pedidos"),
						new Tag("Restaurantes", "Gerencia os restaurantes"),
						new Tag("Estados", "Gerecia os estados"),
						new Tag("Produtos", "Gerencia os produtos de restaurantes"),
				        new Tag("Usuários", "Gerencia os usuários"),
				        new Tag("Estatísticas", "Estatísticas da AlgaFood"),
				        new Tag("Permissões", "Gerencia as permissões"));
	}

	//metodo que configura titulo da documentacao, descricao, versao
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("AlfaFood API")
				.description("API aberta para clientes de restaurantes")
				.version("1")
				.contact(new Contact("Vitor", "http://www.vitor.com.br", "vitoralmeida369369@gmail.com"))
				.build();
	
	}
	
	//aula 18.12
	// metodo para customizar os error de forma global para requisicao to tipo get
	private List<Response> globalGetResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno do Servidor")
						.representation( MediaType.APPLICATION_JSON )
						.apply(getProblemaModelReference())
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que pode ser aceita pelo consumidor")
						.build()
		);
	}

	
	// metodo para customizar os error de forma global para requisicao to tipo post
	private List<Response> globalPostResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno do Servidor")
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que pode ser aceita pelo consumidor")
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Requisição inválida (erro do cliente)")
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
						.description("Requisição recusada porque o corpo está em um formato não suportado")
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build()
						
		);
	}
	
	// metodo para customizar os error de forma global para requisicao to tipo put
	private List<Response> globalPutResponseMessages() {
		return Arrays.asList(
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
						.description("Erro interno do Servidor")
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.NOT_ACCEPTABLE.value()))
						.description("Recurso não possui representação que pode ser aceita pelo consumidor")
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
						.description("Requisição inválida (erro do cliente)")
						.build(),
				new ResponseBuilder()
						.code(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
						.description("Requisição recusada porque o corpo está em um formato não suportado")
						.representation( MediaType.APPLICATION_JSON )
	                    .apply(getProblemaModelReference())
						.build()
						
		);
	}
	
	// metodo para customizar os error de forma global para requisicao to tipo delete
	private List<Response> globalDeleteResponseMessages() {
	    return Arrays.asList(
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
	            .description("Requisição inválida (erro do cliente)")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build(),
	        new ResponseBuilder()
	            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
	            .description("Erro interno no servidor")
	            .representation( MediaType.APPLICATION_JSON )
                .apply(getProblemaModelReference())
	            .build()
	    );
	  }
	
	//aula 18.15
	private Consumer<RepresentationBuilder> getProblemaModelReference() {
	    return r -> r.model(m -> m.name("Problema")
	            .referenceModel(ref -> ref.key(k -> k.qualifiedModelName(
	                    q -> q.name("Problema").namespace("com.algaworks.algafood.api.exceptionhandler")))));
	}
	
	//para fazer com que o SpringFox carregue o módulo de conversão de datas
	@Bean
	public JacksonModuleRegistrar springFoxJacksonConfig() {
		return objectMapper -> objectMapper.registerModule(new JavaTimeModule());
	}

}


//Utilize a URL http://localhost:8080/swagger-ui/index.html .para a swagger
//http://localhost:8080/v3/api-docs

















