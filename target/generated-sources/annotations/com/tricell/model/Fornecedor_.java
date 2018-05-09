package com.tricell.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Fornecedor.class)
public abstract class Fornecedor_ {

	public static volatile ListAttribute<Fornecedor, Centrocusto> centrocustoList;
	public static volatile SingularAttribute<Fornecedor, String> cidade;
	public static volatile SingularAttribute<Fornecedor, Long> idFornecedor;
	public static volatile SingularAttribute<Fornecedor, String> bairro;
	public static volatile SingularAttribute<Fornecedor, Integer> num;
	public static volatile SingularAttribute<Fornecedor, String> cnpjCpf;
	public static volatile SingularAttribute<Fornecedor, String> cep;
	public static volatile SingularAttribute<Fornecedor, String> site;
	public static volatile SingularAttribute<Fornecedor, String> nomeFantasia;
	public static volatile SingularAttribute<Fornecedor, String> logradouro;
	public static volatile SingularAttribute<Fornecedor, String> inscricaoEstadual;
	public static volatile SingularAttribute<Fornecedor, Boolean> inativo;
	public static volatile SingularAttribute<Fornecedor, String> razaoSocial;

}

