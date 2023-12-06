package com.algaworks.algafood.infrastructure.service.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;

//armazenamento local de fotos
//aula 14.8

@Service
public class LocalFotoStorageService implements FotoStorageService {

	//@Value("${algafood.storage.local.diretorio-fotos}")
	//private Path diretorioFotos;
	
	//aula 14.20
	@Autowired
	private StorageProperties storageProperties;

	@Override
	public void armazenar(NovaFoto novaFoto) {

		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

			// pegando a foto e descarregando a foto para o armazenamento local
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possivel armazenar arquivo. ", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		return storageProperties.getLocal().getDiretorioFotos()
				.resolve(Path.of(nomeArquivo));

	}

	//aula 14.10
	//remover arquivos existente do disco local
	@Override
	public void remover(String fotoArquivo) {
		
		try {
		
			Path arquivoPath = getArquivoPath(fotoArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (IOException e) {
			throw new StorageException("Não foi possivel excluir arquivo. ", e);
		}
		
	}
	

	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		Path arquivoPath = getArquivoPath(nomeArquivo);
		try {
			
			FotoRecuperada fotoRecuperada = FotoRecuperada.builder()
					.inputStream(Files.newInputStream(arquivoPath))
					.build();
			
			return fotoRecuperada;
		} catch (IOException e) {
			throw new StorageException("Não foi possivel recuperar o arquivo. ", e);
		}
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
