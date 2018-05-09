package com.tricell.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Orcamento.class)
public abstract class Orcamento_ {

	public static volatile SingularAttribute<Orcamento, Double> vlrTotal;
	public static volatile ListAttribute<Orcamento, Centrocusto> centrocustoList;
	public static volatile SingularAttribute<Orcamento, String> obs;
	public static volatile SingularAttribute<Orcamento, Date> data;
	public static volatile SingularAttribute<Orcamento, Cliente> idCliente;
	public static volatile SingularAttribute<Orcamento, String> idOrcamento;
	public static volatile ListAttribute<Orcamento, Itensorc> itensorcList;
	public static volatile SingularAttribute<Orcamento, Usuario> idUsuario;
	public static volatile SingularAttribute<Orcamento, String> numDoc;
	public static volatile SingularAttribute<Orcamento, Empresa> idEmpresa;
	public static volatile SingularAttribute<Orcamento, String> condPag;
	public static volatile SingularAttribute<Orcamento, String> prazoEnt;

}

