package com.tricell.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Despesas.class)
public abstract class Despesas_ {

	public static volatile ListAttribute<Despesas, Centrocusto> centrocustoList;
	public static volatile SingularAttribute<Despesas, Date> dataPagamento;
	public static volatile SingularAttribute<Despesas, Double> valor;
	public static volatile SingularAttribute<Despesas, Long> idDespesa;
	public static volatile SingularAttribute<Despesas, Date> vencimento;
	public static volatile SingularAttribute<Despesas, Boolean> pago;
	public static volatile SingularAttribute<Despesas, String> descricao;

}

