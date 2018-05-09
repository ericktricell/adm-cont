/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.repository;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tricell.model.Item;
import com.tricell.model.Itensorc;
import com.tricell.model.ItensorcPK;
import com.tricell.model.Orcamento;
import com.tricell.repository.exceptions.NonexistentEntityException;
import com.tricell.repository.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Eu
 */
public class ItensorcJpaController implements Serializable {

    public ItensorcJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Itensorc itensorc) throws PreexistingEntityException, Exception {
        if (itensorc.getItensorcPK() == null) {
            itensorc.setItensorcPK(new ItensorcPK());
        }
        itensorc.getItensorcPK().setIdItem(itensorc.getItem().getIdItem());
        itensorc.getItensorcPK().setIdOrcamento(itensorc.getOrcamento().getIdOrcamento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item item = itensorc.getItem();
            if (item != null) {
                item = em.getReference(item.getClass(), item.getIdItem());
                itensorc.setItem(item);
            }
            Orcamento orcamento = itensorc.getOrcamento();
            if (orcamento != null) {
                orcamento = em.getReference(orcamento.getClass(), orcamento.getIdOrcamento());
                itensorc.setOrcamento(orcamento);
            }
            em.persist(itensorc);
            if (item != null) {
                item.getItensorcList().add(itensorc);
                item = em.merge(item);
            }
            if (orcamento != null) {
                orcamento.getItensorcList().add(itensorc);
                orcamento = em.merge(orcamento);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItensorc(itensorc.getItensorcPK()) != null) {
                throw new PreexistingEntityException("Itensorc " + itensorc + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Itensorc itensorc) throws NonexistentEntityException, Exception {
        itensorc.getItensorcPK().setIdItem(itensorc.getItem().getIdItem());
        itensorc.getItensorcPK().setIdOrcamento(itensorc.getOrcamento().getIdOrcamento());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itensorc persistentItensorc = em.find(Itensorc.class, itensorc.getItensorcPK());
            Item itemOld = persistentItensorc.getItem();
            Item itemNew = itensorc.getItem();
            Orcamento orcamentoOld = persistentItensorc.getOrcamento();
            Orcamento orcamentoNew = itensorc.getOrcamento();
            if (itemNew != null) {
                itemNew = em.getReference(itemNew.getClass(), itemNew.getIdItem());
                itensorc.setItem(itemNew);
            }
            if (orcamentoNew != null) {
                orcamentoNew = em.getReference(orcamentoNew.getClass(), orcamentoNew.getIdOrcamento());
                itensorc.setOrcamento(orcamentoNew);
            }
            itensorc = em.merge(itensorc);
            if (itemOld != null && !itemOld.equals(itemNew)) {
                itemOld.getItensorcList().remove(itensorc);
                itemOld = em.merge(itemOld);
            }
            if (itemNew != null && !itemNew.equals(itemOld)) {
                itemNew.getItensorcList().add(itensorc);
                itemNew = em.merge(itemNew);
            }
            if (orcamentoOld != null && !orcamentoOld.equals(orcamentoNew)) {
                orcamentoOld.getItensorcList().remove(itensorc);
                orcamentoOld = em.merge(orcamentoOld);
            }
            if (orcamentoNew != null && !orcamentoNew.equals(orcamentoOld)) {
                orcamentoNew.getItensorcList().add(itensorc);
                orcamentoNew = em.merge(orcamentoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ItensorcPK id = itensorc.getItensorcPK();
                if (findItensorc(id) == null) {
                    throw new NonexistentEntityException("The itensorc with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ItensorcPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Itensorc itensorc;
            try {
                itensorc = em.getReference(Itensorc.class, id);
                itensorc.getItensorcPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itensorc with id " + id + " no longer exists.", enfe);
            }
            Item item = itensorc.getItem();
            if (item != null) {
                item.getItensorcList().remove(itensorc);
                item = em.merge(item);
            }
            Orcamento orcamento = itensorc.getOrcamento();
            if (orcamento != null) {
                orcamento.getItensorcList().remove(itensorc);
                orcamento = em.merge(orcamento);
            }
            em.remove(itensorc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Itensorc> findItensorcEntities() {
        return findItensorcEntities(true, -1, -1);
    }

    public List<Itensorc> findItensorcEntities(int maxResults, int firstResult) {
        return findItensorcEntities(false, maxResults, firstResult);
    }

    private List<Itensorc> findItensorcEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Itensorc.class));
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

    public Itensorc findItensorc(ItensorcPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Itensorc.class, id);
        } finally {
            em.close();
        }
    }

    public int getItensorcCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Itensorc> rt = cq.from(Itensorc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
