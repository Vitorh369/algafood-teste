package com.algaworks.algafood.domain.service;

import java.util.List;

import com.algaworks.algafood.api.model.dto.VendaDiaria;
import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

//aula 13.13
//implementar as consultas com dados agregados
public interface VendaQueryService {

	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
