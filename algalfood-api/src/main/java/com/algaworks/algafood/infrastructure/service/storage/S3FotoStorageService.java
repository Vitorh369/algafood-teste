package com.algaworks.algafood.infrastructure.service.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

//aula 14.22, 14.23, 14.24, 14,25
@Service
public class S3FotoStorageService implements FotoStorageService{

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties storageProperties;
	
	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
	
		String caminhoArquivo =  getCaminhoArquivo(nomeArquivo);
		
		URL url =amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);
		
		return FotoRecuperada.builder()
				.url(url.toString()).build();
	}

	@Override
	public void remover(String fotoArquivo) {
		
		try {
			String caminhoArquivo = getCaminhoArquivo(fotoArquivo);
			
			var deleteObject = new DeleteObjectRequest(
					storageProperties.getS3().getBucket(), caminhoArquivo);
			
			amazonS3.deleteObject(deleteObject);
			
		} catch (Exception e) {
			throw new StorageException("Não é possivel exluir arquivo na Amazon s3", e);
		}
		
		
		
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
			
			var objectMetada = new ObjectMetadata();
			
			//para abrir o link em vez abaixar foto
			objectMetada.setContentType(novaFoto.getContentType());
			
			// requisicao para por um objeto
			var putObjecrRequest = new PutObjectRequest(
					storageProperties.getS3().getBucket(),
					caminhoArquivo,
					novaFoto.getInputStream(),
					objectMetada)
					//deixando objeto publico quando manda para amazon
					.withCannedAcl(CannedAccessControlList.PublicRead);
			
			amazonS3.putObject(putObjecrRequest);
			
		} catch (Exception e) {
			throw new StorageException("Não foi possivel enviar arquivo para Amazon s3.", e);
		}
		
		
	}
	
	
	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", storageProperties.getS3().getDiretorioFotos(), nomeArquivo);
	}


}
