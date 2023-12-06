package com.algaworks.algafood.core.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

//anotação criada na aula 14.5
public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

	private List<String>allowedContentType;
	
	@Override
	public void initialize(FileContentType constraint) {
		this.allowedContentType = Arrays.asList(constraint.allowed());
	}


	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		return multipartFile == null || this.allowedContentType.contains(multipartFile.getContentType());
	}

}
