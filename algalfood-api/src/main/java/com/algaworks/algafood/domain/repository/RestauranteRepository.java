package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>,  RestauranteRepositoryQueries, 
		JpaSpecificationExecutor<Restaurante>{ //JpaSpecificationExecutor PASSAMOS UM ESPECIFICO, ESSA INTERFACE TEM VARIO METODOS

	//RESCREVEMOS A ASSINATURA DO METODO findALL, CRIANDO UMA Query, PARA BUSCAR APENAS O NECESSARIO! ISSO RESOLVE O PROBLEMA DO N+1
	@Query("from Restaurante r join fetch r.cozinha")
	List<Restaurante> findAll();
	
	List<Restaurante> queryByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);// Between SERVER PRA TRAZER O MENOR E O MAIOR. FUNCIONA COM NUMERO. COMO VALORES E DATA
	
	
//	@Query(value = "from Restaurante where nome like %:nome% and cozinha.id = :id") //COLOCAMOS AS QUERY NA PASTA META-INF -> orm.xml
//	List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinhaId); 

	//List<Restaurante> finByNomeContainingAndCozinhaId(String nome, long cozinhaId);// DEVOLVE O RESTAURANTE E O RELACIONAMENTO BUSCANDO POR ID
	
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome); // VAI FAZER A BUSCA DO PRIMEIRO RESTAURANTE DEE ACORDO COM PARAMETRO PASSADO

	List<Restaurante> findTop2ByNomeContaining(String nome); // BUSCA OS DOIS PRIMEIROS RESTAURANTE 
	
	int countByCozinhaId(Long cozinha); // DEVOLVE A QUANTIDADE DE COZINHA
	
}
