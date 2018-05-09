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
import com.tricell.model.Centrocusto;
import com.tricell.model.Despesas;
import com.tricell.repository.exceptions.IllegalOrphanException;
import com.tricell.repository.exceptions.NonexistentEntityException;
import com.tricell.repository.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Eu
 */
public class DespesasJpaController implements Serializable {

    public DespesasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Despesas despesas) throws PreexistingEntityException, Exception {
        if (despesas.getCentrocustoList() == null) {
            despesas.setCentrocustoList(new ArrayList<Centrocusto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Centrocusto> attachedCentrocustoList = new ArrayList<Centrocusto>();
            for (Centrocusto centrocustoListCentrocustoToAttach : despesas.getCentrocustoList()) {
                centrocustoListCentrocustoToAttach = em.getReference(centrocustoListCentrocustoToAttach.getClass(), centrocustoListCentrocustoToAttach.getIdCentroCusto());
                attachedCentrocustoList.add(centrocustoListCentrocustoToAttach);
            }
            despesas.setCentrocustoList(attachedCentrocustoList);
            em.persist(despesas);
            for (Centrocusto centrocustoListCentrocusto : despesas.getCentrocustoList()) {
                Despesas oldIdDespesaOfCentrocustoListCentrocusto = centrocustoListCentrocusto.getIdDespesa();
                centrocustoListCentrocusto.setIdDespesa(despesas);
                centrocustoListCentrocusto = em.merge(centrocustoListCentrocusto);
                if (oldIdDespesaOfCentrocustoListCentrocusto != null) {
                    oldIdDespesaOfCentrocustoListCentrocusto.getCentrocustoList().remove(centrocustoListCentrocusto);
                    oldIdDespesaOfCentrocustoListCentrocusto = em.merge(oldIdDespesaOfCentrocustoListCentrocusto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDespesas(despesas.getIdDespesa()) != null) {
                throw new PreexistingEntityException("Despesas " + despesas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Despesas despesas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despesas persistentDespesas = em.find(Despesas.class, despesas.getIdDespesa());
            List<Centrocusto> centrocustoListOld = persistentDespesas.getCentrocustoList();
            List<Centrocusto> centrocustoListNew = despesas.getCentrocustoList();
            List<String> illegalOrphanMessages = null;
            for (Centrocusto centrocustoListOldCentrocusto : centrocustoListOld) {
                if (!centrocustoListNew.contains(centrocustoListOldCentrocusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Centrocusto " + centrocustoListOldCentrocusto + " since its idDespesa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Centrocusto> attachedCentrocustoListNew = new ArrayList<Centrocusto>();
            for (Centrocusto centrocustoListNewCentrocustoToAttach : centrocustoListNew) {
                centrocustoListNewCentrocustoToAttach = em.getReference(centrocustoListNewCentrocustoToAttach.getClass(), centrocustoListNewCentrocustoToAttach.getIdCentroCusto());
                attachedCentrocustoListNew.add(centrocustoListNewCentrocustoToAttach);
            }
            centrocustoListNew = attachedCentrocustoListNew;
            despesas.setCentrocustoList(centrocustoListNew);
            despesas = em.merge(despesas);
            for (Centrocusto centrocustoListNewCentrocusto : centrocustoListNew) {
                if (!centrocustoListOld.contains(centrocustoListNewCentrocusto)) {
                    Despesas oldIdDespesaOfCentrocustoListNewCentrocusto = centrocustoListNewCentrocusto.getIdDespesa();
                    centrocustoListNewCentrocusto.setIdDespesa(despesas);
                    centrocustoListNewCentrocusto = em.merge(centrocustoListNewCentrocusto);
                    if (oldIdDespesaOfCentrocustoListNewCentrocusto != null && !oldIdDespesaOfCentrocustoListNewCentrocusto.equals(despesas)) {
                        oldIdDespesaOfCentrocustoListNewCentrocusto.getCentrocustoList().remove(centrocustoListNewCentrocusto);
                        oldIdDespesaOfCentrocustoListNewCentrocusto = em.merge(oldIdDespesaOfCentrocustoListNewCentrocusto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = despesas.getIdDespesa();
                if (findDespesas(id) == null) {
                    throw new NonexistentEntityException("The despesas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Despesas despesas;
            try {
                despesas = em.getReference(Despesas.class, id);
                despesas.getIdDespesa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The despesas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Centrocusto> centrocustoListOrphanCheck = despesas.getCentrocustoList();
            for (Centrocusto centrocustoListOrphanCheckCentrocusto : centrocustoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Despesas (" + despesas + ") cannot be destroyed since the Centrocusto " + centrocustoListOrphanCheckCentrocusto + " in its centrocustoList field has a non-nullable idDespesa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(despesas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Despesas> findDespesasEntities() {
        return findDespesasEntities(true, -1, -1);
    }

    public List<Despesas> findDespesasEntities(int maxResults, int firstResult) {
        return findDespesasEntities(false, maxResults, firstResult);
    }

    private List<Despesas> findDespesasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Despesas.class));
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

    public Despesas findDespesas(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Despesas.class, id);
        } finally {
            em.close();
        }
    }

    public int getDespesasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Despesas> rt = cq.from(Despesas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
