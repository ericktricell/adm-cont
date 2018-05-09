/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Eu
 */
@Entity
@Table(name = "centrocusto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Centrocusto.findAll", query = "SELECT c FROM Centrocusto c")
    , @NamedQuery(name = "Centrocusto.findByIdCentroCusto", query = "SELECT c FROM Centrocusto c WHERE c.idCentroCusto = :idCentroCusto")})
public class Centrocusto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idCentroCusto")
    private Long idCentroCusto;
    @JoinColumn(name = "idDespesa", referencedColumnName = "idDespesa")
    @ManyToOne(optional = false)
    private Despesas idDespesa;
    @JoinColumn(name = "idFornecedor", referencedColumnName = "idFornecedor")
    @ManyToOne(optional = false)
    private Fornecedor idFornecedor;
    @JoinColumn(name = "idOrcamento", referencedColumnName = "idOrcamento")
    @ManyToOne(optional = false)
    private Orcamento idOrcamento;

    public Centrocusto() {
    }

    public Centrocusto(Long idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public Long getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(Long idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public Despesas getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(Despesas idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Fornecedor getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Fornecedor idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Orcamento getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Orcamento idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCentroCusto != null ? idCentroCusto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Centrocusto)) {
            return false;
        }
        Centrocusto other = (Centrocusto) object;
        if ((this.idCentroCusto == null && other.idCentroCusto != null) || (this.idCentroCusto != null && !this.idCentroCusto.equals(other.idCentroCusto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tricell.model.Centrocusto[ idCentroCusto=" + idCentroCusto + " ]";
    }
    
}
