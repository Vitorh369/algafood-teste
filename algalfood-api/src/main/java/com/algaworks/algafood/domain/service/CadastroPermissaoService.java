package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.PermissaoNaoEncontradaException;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.PermissaoRepository;

@Service
public class CadastroPermissaoService {

	@Autowired
	private PermissaoRepository permissaoRepository ;
	
	public Permissao buscarOuFalhar(Long PermissaoId) {
		return permissaoRepository.findById(PermissaoId)
				.orElseThrow(()-> new PermissaoNaoEncontradaException(PermissaoId));
	}
}
