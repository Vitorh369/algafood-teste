package com.algaworks.algafood.core.data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

//aula 13.11
//conversor de propriedades de ordenação
//converte o nome das propriedades
public class PagebleTranslator {

	public static Pageable translate(Pageable pageable, Map<String, String> fieldMapping) {
		
		List<Order> ordenacao = pageable.getSort().stream()
			.filter(ordena -> fieldMapping.containsKey(ordena.getProperty()))
			.map(ordena -> new Sort.Order(
					ordena.getDirection(), fieldMapping.get(ordena.getProperty())))
			.collect(Collectors.toList());
		
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(ordenacao));
	}
}
