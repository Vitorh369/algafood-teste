package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoModel;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

//aula 12.6 em seguida

@Configuration
public class ModelMapperConfig {

	// estou dizendo que agora tem um instancia de ModelMapper dentro do contexto do
	// spring. Para que injeçao do moldeMapper funcione
	
	@Bean
	public ModelMapper modelMapper() {

		var modelMapper = new ModelMapper();

		var enederecoToEnderecoModelTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);
		
		
		enederecoToEnderecoModelTypeMap.<String>addMapping(
				enderecoSRC -> enderecoSRC.getCidade().getEstado().getNome(), 
				(enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));
		
		 modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
	    .addMappings(mapper -> mapper.skip(ItemPedido::setId)); 
		
		return modelMapper;

	}
	
	
}
