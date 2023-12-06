package com.algaworks.algafood2;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import javax.validation.ConstraintViolationException;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncotradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

// tem que segui o sufixo IT para o teste não ser executado na hora do build e adiona no pox.xml o plugin maven-failsafe-plugin
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties") 
public class CadastroCozinhaInteaIT {

//// TESTE DE INTEGRACAO///
	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Test
	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		// cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");

		// ação
		novaCozinha = cadastroCozinha.salvar(novaCozinha);

		// validação
		assertThat(novaCozinha).isNotNull(); // aqui quero validar q quando salva uma cozinha, ela não venha nula
		assertThat(novaCozinha.getId()).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		// cenario
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		// ação
		ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			cadastroCozinha.salvar(novaCozinha);
		});
		// validação
		assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {

		EntidadeEmUsoException erroEsperado = Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
			cadastroCozinha.excluir(1L);
		});

		// validação
		assertThat(erroEsperado).isNotNull();

	}

	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {

		CozinhaNaoEncotradaException erroEsperado = Assertions.assertThrows(CozinhaNaoEncotradaException.class, () -> {
			cadastroCozinha.excluir(100L);
		});

		// validação
		assertThat(erroEsperado).isNotNull();

	}
//////////// ABAIXO TESTE DE API//////////////////////////
	//ADICIONAMOS UMA DEPENDENCIA NO pom.xml PARA USAR OS METODOS PARA TESTE DE API
	
	//como elee roda numa por aleatoria não sendo 8080, criamos essa variavel e passamos para o teste
	@LocalServerPort
	private int port;
	
	@Autowired
	private Flyway flyway; //Voltando o estado inicial do banco de dados para cada execução de teste com callback do Flyway
	
	@BeforeEach // esse teste sera excutado antes
	public void setUp() {
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			RestAssured.port = port;
			RestAssured.basePath = "/cozinhas";
			
			flyway.migrate();
		}
	
	@Test
	public void deveRetornaStatus200_QuandoConsultarCozinhas() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();// se logi falha vai aparece no console
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());//VALIDANDO O STSTUS 200
	}
	
	@Test
	public void deveRetorConter4Cozinhas_QuandoConsultarCozinhas() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();// se logi falha vai aparece no console
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("nome", hasSize(4))// VALIDANDO SE TEM 4 NOMES
			.body("nome", hasItems("Indiana", "Tailandesa"));// VALIDANDO OS NOMES DE COZINHA NO BANCOS DED DADOS
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		
		given()
			.body("{\nome\": \"Chinesa\"}")
			.contentType(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}
}


















