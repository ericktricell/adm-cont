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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "orcamento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orcamento.findAll", query = "SELECT o FROM Orcamento o")
    , @NamedQuery(name = "Orcamento.findByIdOrcamento", query = "SELECT o FROM Orcamento o WHERE o.idOrcamento = :idOrcamento")
    , @NamedQuery(name = "Orcamento.findByCondPag", query = "SELECT o FROM Orcamento o WHERE o.condPag = :condPag")
    , @NamedQuery(name = "Orcamento.findByData", query = "SELECT o FROM Orcamento o WHERE o.data = :data")
    , @NamedQuery(name = "Orcamento.findByNumDoc", query = "SELECT o FROM Orcamento o WHERE o.numDoc = :numDoc")
    , @NamedQuery(name = "Orcamento.findByObs", query = "SELECT o FROM Orcamento o WHERE o.obs = :obs")
    , @NamedQuery(name = "Orcamento.findByPrazoEnt", query = "SELECT o FROM Orcamento o WHERE o.prazoEnt = :prazoEnt")
    , @NamedQuery(name = "Orcamento.findByVlrTotal", query = "SELECT o FROM Orcamento o WHERE o.vlrTotal = :vlrTotal")})
public class Orcamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "idOrcamento")
    private String idOrcamento;
    @Size(max = 50)
    @Column(name = "condPag")
    private String condPag;
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Size(max = 30)
    @Column(name = "numDoc")
    private String numDoc;
    @Size(max = 200)
    @Column(name = "obs")
    private String obs;
    @Size(max = 30)
    @Column(name = "prazoEnt")
    private String prazoEnt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "vlrTotal")
    private Double vlrTotal;
    @JoinColumn(name = "idEmpresa", referencedColumnName = "idEmpresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    @ManyToOne(optional = false)
    private Cliente idCliente;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOrcamento")
    private List<Centrocusto> centrocustoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orcamento")
    private List<Itensorc> itensorcList;

    public Orcamento() {
    }

    public Orcamento(String idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public String getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(String idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public String getCondPag() {
        return condPag;
    }

    public void setCondPag(String condPag) {
        this.condPag = condPag;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPrazoEnt() {
        return prazoEnt;
    }

    public void setPrazoEnt(String prazoEnt) {
        this.prazoEnt = prazoEnt;
    }

    public Double getVlrTotal() {
        return vlrTotal;
    }

    public void setVlrTotal(Double vlrTotal) {
        this.vlrTotal = vlrTotal;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @XmlTransient
    public List<Centrocusto> getCentrocustoList() {
        return centrocustoList;
    }

    public void setCentrocustoList(List<Centrocusto> centrocustoList) {
        this.centrocustoList = centrocustoList;
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
        hash += (idOrcamento != null ? idOrcamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orcamento)) {
            return false;
        }
        Orcamento other = (Orcamento) object;
        if ((this.idOrcamento == null && other.idOrcamento != null) || (this.idOrcamento != null && !this.idOrcamento.equals(other.idOrcamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tricell.model.Orcamento[ idOrcamento=" + idOrcamento + " ]";
    }
    
}
