package com.algaworks.algafood.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("gastronomia") SUBISTIUI O NOME DA CLASS QUANDO FOR FEITO A REQUISIÇÃO

@Data // REPRESENTA, get, setter, equal, hastCode e to String
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	//@NotNull(groups = Groups.CozinhaId.class) //AULA 9.8 groups SERVE PARA MARCACAO. CRIAMOS CLASS OU INTERFACE QUE SERAM GRUPOS DE VALIDAÇÃO
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@JsonIgnore ESCONDE O ATRIBUTO nome QUANDO FOR FEITO A REQUISIÇÃO
	//@JsonProperty(value = "titulo")// ATRIBUTO nome SERA CHAMADO DE titulo QUANDO FOR FEITO A REQUISIÇÃO 
	//@NotBlank
	private String nome;
	
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();

}
