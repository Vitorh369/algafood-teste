package com.algaworks.algafood.core.squiggly;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bohnman.squiggly.Squiggly;
import com.github.bohnman.squiggly.web.RequestSquigglyContextProvider;
import com.github.bohnman.squiggly.web.SquigglyRequestFilter;

//aula 13.3
//class de configuração para ajudar a limitar as propridade usando jackson
// quando é feito uma requisição, o spring passa nesse filtro

@Configuration
public class SquigglyConfig {

	@Bean
	public FilterRegistrationBean<SquigglyRequestFilter> squigglyRequestFilter(ObjectMapper objectMapper){
		
		Squiggly.init(objectMapper, new RequestSquigglyContextProvider("campos", null));
		
		//configurando apenas class que quermos filtro dinamico apenas em pedidos e restaurante
		List<String> urlPatterns = Arrays.asList("/pedidos/*", "/restaurante/*");
		
		FilterRegistrationBean<SquigglyRequestFilter> filterRegistration = new FilterRegistrationBean<SquigglyRequestFilter>();
		filterRegistration.setFilter(new SquigglyRequestFilter());
		filterRegistration.setOrder(1);
		filterRegistration.setUrlPatterns(urlPatterns);
		
		return filterRegistration;
	}
}
