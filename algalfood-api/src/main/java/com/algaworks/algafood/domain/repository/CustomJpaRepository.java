package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

// ESSA INTERFACE É UM REPOSITORIO BASE QUE VAI SER PRA TODOS OS REPOSITORIO

@NoRepositoryBean // SPRING NÃO DEVE INSTACIAR NADA NESSA INTERFACE
public interface CustomJpaRepository <T, ID> extends JpaRepository<T, ID> {

	Optional<T> buscarPrimiero();
	
	// metodo para desconectar o jpa
	void detach(T entity);
	
}
