package com.tricell.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Empresa.class)
public abstract class Empresa_ {

	public static volatile SingularAttribute<Empresa, String> uf;
	public static volatile SingularAttribute<Empresa, String> cidade;
	public static volatile SingularAttribute<Empresa, String> telefone;
	public static volatile SingularAttribute<Empresa, String> nomeFantasia;
	public static volatile ListAttribute<Empresa, Orcamento> orcamentoList;
	public static volatile SingularAttribute<Empresa, String> bairro;
	public static volatile SingularAttribute<Empresa, String> logradouro;
	public static volatile SingularAttribute<Empresa, Integer> num;
	public static volatile SingularAttribute<Empresa, Long> idEmpresa;
	public static volatile SingularAttribute<Empresa, String> cnpj;
	public static volatile SingularAttribute<Empresa, String> razaoSocial;
	public static volatile SingularAttribute<Empresa, String> cep;

}

