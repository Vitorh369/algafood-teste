package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.filter.VendaDiariaFilter;

//aula 13.19  implementando relatorio
public interface VendaReportService {

	byte[] emitirVendaDiarias(VendaDiariaFilter filtro, String timeOffset);
}
