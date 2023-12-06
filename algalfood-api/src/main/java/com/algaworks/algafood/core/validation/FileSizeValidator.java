package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

//anotação criada na aula 14.3
public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

	// lida com conversão de KB,MB e etc..
	private DataSize maxSize;

	// fazendo parse de srtring para um KB
	@Override
	public void initialize(FileSize constraintAnnotation) {
		this.maxSize = DataSize.parse(constraintAnnotation.max());
	}

	// validando o tamnho do arquivo
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		return value == null || value.getSize() <= this.maxSize.toBytes();
	}

}
