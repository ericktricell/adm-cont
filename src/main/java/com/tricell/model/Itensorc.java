/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eu
 */
@Entity
@Table(name = "itensorc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Itensorc.findAll", query = "SELECT i FROM Itensorc i")
    , @NamedQuery(name = "Itensorc.findByIdOrcamento", query = "SELECT i FROM Itensorc i WHERE i.itensorcPK.idOrcamento = :idOrcamento")
    , @NamedQuery(name = "Itensorc.findByIdItem", query = "SELECT i FROM Itensorc i WHERE i.itensorcPK.idItem = :idItem")
    , @NamedQuery(name = "Itensorc.findByNum", query = "SELECT i FROM Itensorc i WHERE i.num = :num")})
public class Itensorc implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItensorcPK itensorcPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num")
    private double num;
    @JoinColumn(name = "idItem", referencedColumnName = "idItem", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Item item;
    @JoinColumn(name = "idOrcamento", referencedColumnName = "idOrcamento", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Orcamento orcamento;

    public Itensorc() {
    }

    public Itensorc(ItensorcPK itensorcPK) {
        this.itensorcPK = itensorcPK;
    }

    public Itensorc(ItensorcPK itensorcPK, double num) {
        this.itensorcPK = itensorcPK;
        this.num = num;
    }

    public Itensorc(String idOrcamento, long idItem) {
        this.itensorcPK = new ItensorcPK(idOrcamento, idItem);
    }

    public ItensorcPK getItensorcPK() {
        return itensorcPK;
    }

    public void setItensorcPK(ItensorcPK itensorcPK) {
        this.itensorcPK = itensorcPK;
    }

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itensorcPK != null ? itensorcPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Itensorc)) {
            return false;
        }
        Itensorc other = (Itensorc) object;
        if ((this.itensorcPK == null && other.itensorcPK != null) || (this.itensorcPK != null && !this.itensorcPK.equals(other.itensorcPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tricell.model.Itensorc[ itensorcPK=" + itensorcPK + " ]";
    }
    
}
