package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

//aula 14.6
public interface ProdutoRepositoryQueries {
	
	FotoProduto save(FotoProduto foto);
	
	void delete (FotoProduto foto);
}
