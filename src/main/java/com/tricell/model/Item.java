/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eu
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
    , @NamedQuery(name = "Item.findByIdItem", query = "SELECT i FROM Item i WHERE i.idItem = :idItem")
    , @NamedQuery(name = "Item.findByDiscriminacao", query = "SELECT i FROM Item i WHERE i.discriminacao = :discriminacao")
    , @NamedQuery(name = "Item.findByTipo", query = "SELECT i FROM Item i WHERE i.tipo = :tipo")
    , @NamedQuery(name = "Item.findByUn", query = "SELECT i FROM Item i WHERE i.un = :un")
    , @NamedQuery(name = "Item.findByVlrUnit", query = "SELECT i FROM Item i WHERE i.vlrUnit = :vlrUnit")})
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idItem")
    private Long idItem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "discriminacao")
    private String discriminacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "un")
    private String un;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vlrUnit")
    private double vlrUnit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private List<Itensorc> itensorcList;

    public Item() {
    }

    public Item(Long idItem) {
        this.idItem = idItem;
    }

    public Item(Long idItem, String discriminacao, String tipo, String un, double vlrUnit) {
        this.idItem = idItem;
        this.discriminacao = discriminacao;
        this.tipo = tipo;
        this.un = un;
        this.vlrUnit = vlrUnit;
    }

    public Long getIdItem() {
        return idItem;
    }

    public void setIdItem(Long idItem) {
        this.idItem = idItem;
    }

    public String getDiscriminacao() {
        return discriminacao;
    }

    public void setDiscriminacao(String discriminacao) {
        this.discriminacao = discriminacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public double getVlrUnit() {
        return vlrUnit;
    }

    public void setVlrUnit(double vlrUnit) {
        this.vlrUnit = vlrUnit;
    }

    @XmlTransient
    public List<Itensorc> getItensorcList() {
        return itensorcList;
    }

    public void setItensorcList(List<Itensorc> itensorcList) {
        this.itensorcList = itensorcList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItem != null ? idItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.idItem == null && other.idItem != null) || (this.idItem != null && !this.idItem.equals(other.idItem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tricell.model.Item[ idItem=" + idItem + " ]";
    }
    
}
