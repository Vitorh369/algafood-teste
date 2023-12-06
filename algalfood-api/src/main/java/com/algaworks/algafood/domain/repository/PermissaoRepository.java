package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.model.Permissao;

@Service
public interface PermissaoRepository extends JpaRepository<Permissao, Long>{

}
