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
import com.tricell.model.Fornecedor;
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
public class FornecedorJpaController implements Serializable {

    public FornecedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Fornecedor fornecedor) throws PreexistingEntityException, Exception {
        if (fornecedor.getCentrocustoList() == null) {
            fornecedor.setCentrocustoList(new ArrayList<Centrocusto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Centrocusto> attachedCentrocustoList = new ArrayList<Centrocusto>();
            for (Centrocusto centrocustoListCentrocustoToAttach : fornecedor.getCentrocustoList()) {
                centrocustoListCentrocustoToAttach = em.getReference(centrocustoListCentrocustoToAttach.getClass(), centrocustoListCentrocustoToAttach.getIdCentroCusto());
                attachedCentrocustoList.add(centrocustoListCentrocustoToAttach);
            }
            fornecedor.setCentrocustoList(attachedCentrocustoList);
            em.persist(fornecedor);
            for (Centrocusto centrocustoListCentrocusto : fornecedor.getCentrocustoList()) {
                Fornecedor oldIdFornecedorOfCentrocustoListCentrocusto = centrocustoListCentrocusto.getIdFornecedor();
                centrocustoListCentrocusto.setIdFornecedor(fornecedor);
                centrocustoListCentrocusto = em.merge(centrocustoListCentrocusto);
                if (oldIdFornecedorOfCentrocustoListCentrocusto != null) {
                    oldIdFornecedorOfCentrocustoListCentrocusto.getCentrocustoList().remove(centrocustoListCentrocusto);
                    oldIdFornecedorOfCentrocustoListCentrocusto = em.merge(oldIdFornecedorOfCentrocustoListCentrocusto);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFornecedor(fornecedor.getIdFornecedor()) != null) {
                throw new PreexistingEntityException("Fornecedor " + fornecedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Fornecedor fornecedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Fornecedor persistentFornecedor = em.find(Fornecedor.class, fornecedor.getIdFornecedor());
            List<Centrocusto> centrocustoListOld = persistentFornecedor.getCentrocustoList();
            List<Centrocusto> centrocustoListNew = fornecedor.getCentrocustoList();
            List<String> illegalOrphanMessages = null;
            for (Centrocusto centrocustoListOldCentrocusto : centrocustoListOld) {
                if (!centrocustoListNew.contains(centrocustoListOldCentrocusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Centrocusto " + centrocustoListOldCentrocusto + " since its idFornecedor field is not nullable.");
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
            fornecedor.setCentrocustoList(centrocustoListNew);
            fornecedor = em.merge(fornecedor);
            for (Centrocusto centrocustoListNewCentrocusto : centrocustoListNew) {
                if (!centrocustoListOld.contains(centrocustoListNewCentrocusto)) {
                    Fornecedor oldIdFornecedorOfCentrocustoListNewCentrocusto = centrocustoListNewCentrocusto.getIdFornecedor();
                    centrocustoListNewCentrocusto.setIdFornecedor(fornecedor);
                    centrocustoListNewCentrocusto = em.merge(centrocustoListNewCentrocusto);
                    if (oldIdFornecedorOfCentrocustoListNewCentrocusto != null && !oldIdFornecedorOfCentrocustoListNewCentrocusto.equals(fornecedor)) {
                        oldIdFornecedorOfCentrocustoListNewCentrocusto.getCentrocustoList().remove(centrocustoListNewCentrocusto);
                        oldIdFornecedorOfCentrocustoListNewCentrocusto = em.merge(oldIdFornecedorOfCentrocustoListNewCentrocusto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = fornecedor.getIdFornecedor();
                if (findFornecedor(id) == null) {
                    throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.");
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
            Fornecedor fornecedor;
            try {
                fornecedor = em.getReference(Fornecedor.class, id);
                fornecedor.getIdFornecedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The fornecedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Centrocusto> centrocustoListOrphanCheck = fornecedor.getCentrocustoList();
            for (Centrocusto centrocustoListOrphanCheckCentrocusto : centrocustoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Fornecedor (" + fornecedor + ") cannot be destroyed since the Centrocusto " + centrocustoListOrphanCheckCentrocusto + " in its centrocustoList field has a non-nullable idFornecedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(fornecedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Fornecedor> findFornecedorEntities() {
        return findFornecedorEntities(true, -1, -1);
    }

    public List<Fornecedor> findFornecedorEntities(int maxResults, int firstResult) {
        return findFornecedorEntities(false, maxResults, firstResult);
    }

    private List<Fornecedor> findFornecedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Fornecedor.class));
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

    public Fornecedor findFornecedor(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Fornecedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getFornecedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Fornecedor> rt = cq.from(Fornecedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
