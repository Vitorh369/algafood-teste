package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;

//JpaSpecificationExecutor extendemos para  utilizar o filtro aula 13.6
@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>,
	JpaSpecificationExecutor<Pedido>{
	
	Optional<Pedido>findByCodigo(String ccoddigo);

	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
	List<Pedido> finAll();
}      

