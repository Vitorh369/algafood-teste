package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

//@ValorZeroIncluiDescricao(valorField="taxaFrete", descricaoField="nome", descricaoObrigatoria="Frete Grátis")
@Data // REPRESENTA, get, setter, equal, hastCode e to String
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include // INCLUINDO APENAS O id PARA EqualsAndHashCode
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotBlank 
	@Column(nullable = false) //NÃO ACEITA nome NULO
	private String nome;

	//@DecimalMin("0")
	//@TaxaFrete anotacao criada
	//@NotNull
	//@PositiveOrZero//TEM QUE SER ZERO OU MAIOR Q ZERO
	//@Multiplo(numero = 5) // anotacao criada
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	//@Valid // VAI VALIDA AS PROPRIEDADES DE COZINHA, PARA UMA VALIDAÇÃO EM CASCATA
	//@ConvertGroup(from = Default.class, to =Groups.CozinhaId.class)// VALIDAÇAO EM CASCATA. FAZEMOS A CONVERSAO DO Default PARA Groups.CadastroRestaurante.class
	//@NotNull
	@JoinColumn(name = "cozinha_id", nullable = false)// NOME DA COLUNA COM RELACIONAMENTO! PODEMOS POR NOME Q QUISER, MAS POR PADRÃO JA VAI SER cozinha_id
	@ManyToOne //(fetch = FetchType.LAZY)//lazy CARREGAMENTO PREGUIÇOSO
	private Cozinha cozinha;
	
	
	@Embedded // O ENDEREÇO É UMA PARTE DA CLASS RESTAURANTE 
	private Endereco endereco;
	
	private Boolean ativo =  Boolean.TRUE;
	
	private Boolean aberto = Boolean.FALSE;
	
	
	@CreationTimestamp //  QUANDO SALVAR PELA PRIMEIRA VEZ, VAI ATRIBUIR UMA DATA AUTOMATICAMENTE 
	@Column(nullable = false, columnDefinition = "datetime")//(nullable = false) que dizer q a propridade é obrigatoria
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp // VAI ATUALIZAR A DATA, TODA VEZ QUE UM RECURSO FOR ATUALIZADO
	@Column(nullable = false, columnDefinition = "datetime") // columnDefinition = "datetime" para ignora os milisegundos
	private OffsetDateTime data_atualizacao;
	
	//usamos o set e HashSet para não aceita elemento duplicado
	@ManyToMany // VAI SER CRIADO UMA TERCEIRA TABELA, PARA GERENCIAR OS DOIS RELEACIONAMENTO! CHAVES ESTRANGEIRAS FormaPagamento_id E Restaurante_id 
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name="restaurante_id"),
			inverseJoinColumns = @JoinColumn(name="forma_pagamento_id")) // NOME DA TABELA INTERMEDIARIA E O NOME DAS CHAVES ESTRANGEIRAS
	private Set<FormaPagamento> formasPagamentos = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();
	
	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
				joinColumns = @JoinColumn(name = "restaurante_id"),
				inverseJoinColumns = @JoinColumn(name= "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	public void ativar() {
		setAtivo(true);
	}
	
	public void inativar() {
		setAtivo(false);
	}
	
	public boolean removerFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamentos().remove(formaPagamento);
	}
	
	public boolean adicionarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamentos().add(formaPagamento);
	}
	
	public void abrir() {
		setAberto(true);
	}
	
	public void fechar() {
		setAberto(false);
	}
	
	public boolean removeResponsavel(Usuario usuario) {
		return getResponsaveis().remove(usuario);
	}
	
	public boolean adcionarResponsavel(Usuario usuario) {
		return getResponsaveis().add(usuario);
	}
	
	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return getFormasPagamentos().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
	    return !aceitaFormaPagamento(formaPagamento);
	}
	
	public boolean isAberto() {
	    return this.aberto;
	}

	public boolean isFechado() {
	    return !isAberto();
	}

	public boolean isInativo() {
	    return !isAtivo();
	}

	public boolean isAtivo() {
	    return this.ativo;
	}

	public boolean aberturaPermitida() {
	    return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
	    return isInativo();
	}
	
	public boolean inativacaoPermitida() {
	    return isAtivo();
	}

	public boolean fechamentoPermitido() {
	    return isAberto();
	}    
}





