/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Eu
 */
@Entity
@Table(name = "despesas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Despesas.findAll", query = "SELECT d FROM Despesas d")
    , @NamedQuery(name = "Despesas.findByIdDespesa", query = "SELECT d FROM Despesas d WHERE d.idDespesa = :idDespesa")
    , @NamedQuery(name = "Despesas.findByDataPagamento", query = "SELECT d FROM Despesas d WHERE d.dataPagamento = :dataPagamento")
    , @NamedQuery(name = "Despesas.findByDescricao", query = "SELECT d FROM Despesas d WHERE d.descricao = :descricao")
    , @NamedQuery(name = "Despesas.findByPago", query = "SELECT d FROM Despesas d WHERE d.pago = :pago")
    , @NamedQuery(name = "Despesas.findByValor", query = "SELECT d FROM Despesas d WHERE d.valor = :valor")
    , @NamedQuery(name = "Despesas.findByVencimento", query = "SELECT d FROM Despesas d WHERE d.vencimento = :vencimento")})
public class Despesas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idDespesa")
    private Long idDespesa;
    @Column(name = "dataPagamento")
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "pago")
    private Boolean pago;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private double valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vencimento")
    @Temporal(TemporalType.DATE)
    private Date vencimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDespesa")
    private List<Centrocusto> centrocustoList;

    public Despesas() {
    }

    public Despesas(Long idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Despesas(Long idDespesa, String descricao, double valor, Date vencimento) {
        this.idDespesa = idDespesa;
        this.descricao = descricao;
        this.valor = valor;
        this.vencimento = vencimento;
    }

    public Long getIdDespesa() {
        return idDespesa;
    }

    public void setIdDespesa(Long idDespesa) {
        this.idDespesa = idDespesa;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    @XmlTransient
    public List<Centrocusto> getCentrocustoList() {
        return centrocustoList;
    }

    public void setCentrocustoList(List<Centrocusto> centrocustoList) {
        this.centrocustoList = centrocustoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDespesa != null ? idDespesa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Despesas)) {
            return false;
        }
        Despesas other = (Despesas) object;
        if ((this.idDespesa == null && other.idDespesa != null) || (this.idDespesa != null && !this.idDespesa.equals(other.idDespesa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tricell.model.Despesas[ idDespesa=" + idDespesa + " ]";
    }
    
}
