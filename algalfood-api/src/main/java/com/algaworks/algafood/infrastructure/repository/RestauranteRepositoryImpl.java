package com.algaworks.algafood.infrastructure.repository;

import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

// SEMPRE COLOCA NO FINAL DO NOME DA CLASS "Impl", PQ É UM SUFIXO QUE DIZ QUE ESSA CLASS É UMA IMPLEMENTAÇÃO
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired @Lazy // lazy SERVE PARA UTILIZAR O RestauranteRepository! COMO CLASS RestauranteRepository JA É UM FRAGMENTO DESSA CLASS RestauranteRepositoryImpl. PARA NAO DA CONFUSÃO DE UM LOOP, UTLIZAMOS ESA ANOTAÇÃO! PARA INSTANCIAR RestauranteRepository SOMENTE QUANDO FOR PRECISO
	private RestauranteRepository repository;

	@Override
	public List<Restaurante> find(String nome,
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder(); //interface QUE CONNSTROI ELELMENTOS PARA FAZERMOS CONSULTA
		
		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class); //interface RESPONSAVEL POR MONTAR A ESTRUTURA DED UMA QUERY
		
		Root<Restaurante> root =  criteria.from(Restaurante.class);// from Restaurante
		
		ArrayList<Predicate> predicates = new ArrayList<Predicate>();
		
		if(StringUtils.hasText(nome)) {
			predicates.add( builder.like(root.get("nome"), "%" + nome + "%")); // % PODE SER ANTES OU DEPOIS
		}
		
		if(taxaFreteInicial != null) {
			predicates.add( builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));// MAIOR OU IGUAL
		}
		
		if(taxaFreteFinal != null) {
			predicates.add( builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));// MENOR OU IGUAL
		}
		
		criteria.where(predicates.toArray(new Predicate [0])); // PASSAR UMA LISTA DE Predicate VAZIA PRA RECERBER OS PARAMENNTROS (nome, taxaFreteInicial, taxaFreteFinal )
		
		 TypedQuery<Restaurante> query = entityManager.createQuery(criteria);
		  return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		
		return repository.findAll(comFreteGratis()
				.and(comNomeSemelhante(nome)));
	}
}
