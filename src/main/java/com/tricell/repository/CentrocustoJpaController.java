/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.repository;

import com.tricell.model.Centrocusto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tricell.model.Despesas;
import com.tricell.model.Fornecedor;
import com.tricell.model.Orcamento;
import com.tricell.repository.exceptions.NonexistentEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Eu
 */
public class CentrocustoJpaController implements Serializable {

    public CentrocustoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Centrocusto centrocusto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despesas idDespesa = centrocusto.getIdDespesa();
            if (idDespesa != null) {
                idDespesa = em.getReference(idDespesa.getClass(), idDespesa.getIdDespesa());
                centrocusto.setIdDespesa(idDespesa);
            }
            Fornecedor idFornecedor = centrocusto.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor = em.getReference(idFornecedor.getClass(), idFornecedor.getIdFornecedor());
                centrocusto.setIdFornecedor(idFornecedor);
            }
            Orcamento idOrcamento = centrocusto.getIdOrcamento();
            if (idOrcamento != null) {
                idOrcamento = em.getReference(idOrcamento.getClass(), idOrcamento.getIdOrcamento());
                centrocusto.setIdOrcamento(idOrcamento);
            }
            em.persist(centrocusto);
            if (idDespesa != null) {
                idDespesa.getCentrocustoList().add(centrocusto);
                idDespesa = em.merge(idDespesa);
            }
            if (idFornecedor != null) {
                idFornecedor.getCentrocustoList().add(centrocusto);
                idFornecedor = em.merge(idFornecedor);
            }
            if (idOrcamento != null) {
                idOrcamento.getCentrocustoList().add(centrocusto);
                idOrcamento = em.merge(idOrcamento);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Centrocusto centrocusto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centrocusto persistentCentrocusto = em.find(Centrocusto.class, centrocusto.getIdCentroCusto());
            Despesas idDespesaOld = persistentCentrocusto.getIdDespesa();
            Despesas idDespesaNew = centrocusto.getIdDespesa();
            Fornecedor idFornecedorOld = persistentCentrocusto.getIdFornecedor();
            Fornecedor idFornecedorNew = centrocusto.getIdFornecedor();
            Orcamento idOrcamentoOld = persistentCentrocusto.getIdOrcamento();
            Orcamento idOrcamentoNew = centrocusto.getIdOrcamento();
            if (idDespesaNew != null) {
                idDespesaNew = em.getReference(idDespesaNew.getClass(), idDespesaNew.getIdDespesa());
                centrocusto.setIdDespesa(idDespesaNew);
            }
            if (idFornecedorNew != null) {
                idFornecedorNew = em.getReference(idFornecedorNew.getClass(), idFornecedorNew.getIdFornecedor());
                centrocusto.setIdFornecedor(idFornecedorNew);
            }
            if (idOrcamentoNew != null) {
                idOrcamentoNew = em.getReference(idOrcamentoNew.getClass(), idOrcamentoNew.getIdOrcamento());
                centrocusto.setIdOrcamento(idOrcamentoNew);
            }
            centrocusto = em.merge(centrocusto);
            if (idDespesaOld != null && !idDespesaOld.equals(idDespesaNew)) {
                idDespesaOld.getCentrocustoList().remove(centrocusto);
                idDespesaOld = em.merge(idDespesaOld);
            }
            if (idDespesaNew != null && !idDespesaNew.equals(idDespesaOld)) {
                idDespesaNew.getCentrocustoList().add(centrocusto);
                idDespesaNew = em.merge(idDespesaNew);
            }
            if (idFornecedorOld != null && !idFornecedorOld.equals(idFornecedorNew)) {
                idFornecedorOld.getCentrocustoList().remove(centrocusto);
                idFornecedorOld = em.merge(idFornecedorOld);
            }
            if (idFornecedorNew != null && !idFornecedorNew.equals(idFornecedorOld)) {
                idFornecedorNew.getCentrocustoList().add(centrocusto);
                idFornecedorNew = em.merge(idFornecedorNew);
            }
            if (idOrcamentoOld != null && !idOrcamentoOld.equals(idOrcamentoNew)) {
                idOrcamentoOld.getCentrocustoList().remove(centrocusto);
                idOrcamentoOld = em.merge(idOrcamentoOld);
            }
            if (idOrcamentoNew != null && !idOrcamentoNew.equals(idOrcamentoOld)) {
                idOrcamentoNew.getCentrocustoList().add(centrocusto);
                idOrcamentoNew = em.merge(idOrcamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = centrocusto.getIdCentroCusto();
                if (findCentrocusto(id) == null) {
                    throw new NonexistentEntityException("The centrocusto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Centrocusto centrocusto;
            try {
                centrocusto = em.getReference(Centrocusto.class, id);
                centrocusto.getIdCentroCusto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The centrocusto with id " + id + " no longer exists.", enfe);
            }
            Despesas idDespesa = centrocusto.getIdDespesa();
            if (idDespesa != null) {
                idDespesa.getCentrocustoList().remove(centrocusto);
                idDespesa = em.merge(idDespesa);
            }
            Fornecedor idFornecedor = centrocusto.getIdFornecedor();
            if (idFornecedor != null) {
                idFornecedor.getCentrocustoList().remove(centrocusto);
                idFornecedor = em.merge(idFornecedor);
            }
            Orcamento idOrcamento = centrocusto.getIdOrcamento();
            if (idOrcamento != null) {
                idOrcamento.getCentrocustoList().remove(centrocusto);
                idOrcamento = em.merge(idOrcamento);
            }
            em.remove(centrocusto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Centrocusto> findCentrocustoEntities() {
        return findCentrocustoEntities(true, -1, -1);
    }

    public List<Centrocusto> findCentrocustoEntities(int maxResults, int firstResult) {
        return findCentrocustoEntities(false, maxResults, firstResult);
    }

    private List<Centrocusto> findCentrocustoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Centrocusto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Centrocusto findCentrocusto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Centrocusto.class, id);
        } finally {
            em.close();
        }
    }

    public int getCentrocustoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Centrocusto> rt = cq.from(Centrocusto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
