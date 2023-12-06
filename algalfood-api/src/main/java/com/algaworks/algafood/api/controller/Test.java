package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;


@RestController
@RequestMapping("test")
public class Test {
	
	@Autowired
	private RestauranteRepository repository;
	
//	@GetMapping
//	public List<Restaurante> buscarPorNome(String nome, Long cozinhaId) {
//		return repository.consultarPorNome(nome, cozinhaId);
//	}
	
	@GetMapping("/restaurantePorNome")
	public List<Restaurante> buscarPorNomeFrete(String nome, 
			BigDecimal taxaFreteIncial, BigDecimal taxaFretefinal) {
		
		return repository.find(nome, taxaFreteIncial, taxaFretefinal);
	}
	
	@GetMapping("/comFreteGratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome) {
		return repository.findComFreteGratis(nome);
	}

	@GetMapping("/restaurante/primeiro")
	public Optional<Restaurante> restaurantrPrimeiro(){
		return repository.buscarPrimiero();
				
	}

}
