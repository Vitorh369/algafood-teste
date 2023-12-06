package com.algaworks.algafood.domain.event;

import com.algaworks.algafood.domain.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Getter;

//aula 15.11
@AllArgsConstructor
@Getter
public class PedidoConfirmadoEvent {

	private Pedido pedido;
}
