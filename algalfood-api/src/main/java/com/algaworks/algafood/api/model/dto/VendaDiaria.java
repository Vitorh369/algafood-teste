package com.algaworks.algafood.api.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//aula 13.13
//implementar as consultas com dados agregados

@AllArgsConstructor
@Getter
@Setter
public class VendaDiaria {

	private Date data;
	private Long totalVendas;
	private BigDecimal totalFatura;
}
