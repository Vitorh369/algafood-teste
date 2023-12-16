package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.model.FotoProdutoModel;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncotradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CatalogoFotoProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.algaworks.algafood.domain.service.FotoStorageService.FotoRecuperada;

//aula 14.6

@RestController
@RequestMapping("/restaurante/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi{

	@Autowired
	private CadastroProdutoService cadastroProdutoService;

	@Autowired
	private CatalogoFotoProdutoService catalogoFotoProdutoService;

	@Autowired
	private FotoProdutoModelAssembler fotoProdutoModelAssembler;
	
	@Autowired
	private FotoStorageService fotoStorageService;

	@Override
	//adionar a foto
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId,
			@Valid FotoProdutoInput fotoProdutoInput, @RequestPart(required = true) MultipartFile arquivo) throws IOException {

		Produto produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);

		//MultipartFile arquivo = fotoProdutoInput.getArquivo();

		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setDescricao(fotoProdutoInput.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		foto.setNomeArquivo(arquivo.getOriginalFilename());

		FotoProduto fotoSalva = catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream());

		return fotoProdutoModelAssembler.toModel(fotoSalva);

	}
	
	//buscar dados da foto
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto  foto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
		return fotoProdutoModelAssembler.toModel(foto);
		
	}
	
	//aula 14.13, 14.14
	//buscar a imagem para mostrar
	@GetMapping
	public ResponseEntity<?> servir(@PathVariable Long restauranteId,
			@PathVariable Long produtoId, @RequestHeader(name = "accept") String accepHeader) throws HttpMediaTypeNotAcceptableException {
		
		try {
			FotoProduto  foto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);
			
			// pega o tipo de midia q esta armazenado
			MediaType mediaTypeFoto = MediaType.parseMediaType(foto.getContentType());
			List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(accepHeader);
			
			//verificar se um tipo q esta na lista
			varificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypeAceitas);
			
			FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(foto.getNomeArquivo());
			
			//se a foto tiver um url vai buscar na amazon
			if(fotoRecuperada.temUrl()) {
				return ResponseEntity
						.status(HttpStatus.FOUND)
						.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
						.build();
			
			} else {
				return ResponseEntity.ok()
						.contentType(mediaTypeFoto)
						.body(new InputStreamResource(fotoRecuperada.getInputStream()));
			}
		
		} catch (EntidadeNaoEncotradaException e) {
			return ResponseEntity.notFound().build();
		}

	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		catalogoFotoProdutoService.excluir(restauranteId, produtoId);
	}

	//aula 14.14
	//verifica se a foto é compative
	private void varificarCompatibilidadeMediaType(MediaType mediaTypeFoto, 
			List<MediaType> mediaTypeAceitas) throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediaTypeAceitas.stream()
				.anyMatch(mediaTypeAceita ->  mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypeAceitas);
			
		}
		
	}



}






















