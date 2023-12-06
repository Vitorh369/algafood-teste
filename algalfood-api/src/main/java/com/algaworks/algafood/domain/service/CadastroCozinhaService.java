package com.algaworks.algafood.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncotradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

	private static final String MSG_COZINHA_EM_USO = "Cozinha de codigo %d não pode ser removido, pois esta em uso";

	
	@Autowired
	private CozinhaRepository cozinhaRepository;

	@Transactional// boa pratica anotatar com trasantion os metodos publico que altera algo no banco de dados
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);

	}

	@Transactional// boa pratica anotatar com trasantion os metodos publico que altera algo no banco de dados
	public void excluir(Long cozinhaId) {
		try {
			cozinhaRepository.deleteById(cozinhaId);
			cozinhaRepository.flush(); // para o metodo excluiir. para descarregar oq ta na fila
			
		} catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncotradaException(cozinhaId);
		
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format(MSG_COZINHA_EM_USO, cozinhaId));
		}
	}
	
	public Cozinha BuscarOuFalhar(Long cozinhaId) {
		return cozinhaRepository.findById(cozinhaId)
				.orElseThrow(() -> new CozinhaNaoEncotradaException(cozinhaId));// vai retorna 200 se não tiver cozinha retorna 404
	}
}