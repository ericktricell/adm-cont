package com.tricell.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Item.class)
public abstract class Item_ {

	public static volatile SingularAttribute<Item, String> tipo;
	public static volatile SingularAttribute<Item, String> discriminacao;
	public static volatile ListAttribute<Item, Itensorc> itensorcList;
	public static volatile SingularAttribute<Item, String> un;
	public static volatile SingularAttribute<Item, Double> vlrUnit;
	public static volatile SingularAttribute<Item, Long> idItem;

}

