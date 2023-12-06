package com.algaworks.algafood.domain.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CadastroGrupoService cadastroGrupo;
    
    @Transactional
    public Usuario salvar(Usuario usuario) {
    	
    	// metodo para desconectar o jpa
    	usuarioRepository.detach(usuario);
    	
    	Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
    	
    	//verificando se usuario existe e verificando se o usuario tem um email igual a outro usuario do banco de dados
    	if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario) ) {
    		throw new NegocioException(
    				String.format("Já existe um usuário cadastrado com o email %s", usuario.getEmail()));
    	}
    	
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        
        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        
        usuario.setSenha(novaSenha);
    }
    
    @Transactional
    public void desassociarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOufalhar(grupoId);
        
        usuario.removerGrupo(grupo);
    }
    
    @Transactional
    public void associarGrupo(Long usuarioId, Long grupoId) {
    	Usuario usuario =  buscarOuFalhar(usuarioId);
    	Grupo grupo = cadastroGrupo.buscarOufalhar(grupoId);
    	
    	usuario.adicionarGrupo(grupo);
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
    }            
}
















