package com.tricell.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Centrocusto.class)
public abstract class Centrocusto_ {

	public static volatile SingularAttribute<Centrocusto, Long> idCentroCusto;
	public static volatile SingularAttribute<Centrocusto, Fornecedor> idFornecedor;
	public static volatile SingularAttribute<Centrocusto, Orcamento> idOrcamento;
	public static volatile SingularAttribute<Centrocusto, Despesas> idDespesa;

}

