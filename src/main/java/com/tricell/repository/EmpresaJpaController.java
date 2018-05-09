/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.repository;

import com.tricell.model.Empresa;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tricell.model.Orcamento;
import com.tricell.repository.exceptions.IllegalOrphanException;
import com.tricell.repository.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Eu
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) {
        if (empresa.getOrcamentoList() == null) {
            empresa.setOrcamentoList(new ArrayList<Orcamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Orcamento> attachedOrcamentoList = new ArrayList<Orcamento>();
            for (Orcamento orcamentoListOrcamentoToAttach : empresa.getOrcamentoList()) {
                orcamentoListOrcamentoToAttach = em.getReference(orcamentoListOrcamentoToAttach.getClass(), orcamentoListOrcamentoToAttach.getIdOrcamento());
                attachedOrcamentoList.add(orcamentoListOrcamentoToAttach);
            }
            empresa.setOrcamentoList(attachedOrcamentoList);
            em.persist(empresa);
            for (Orcamento orcamentoListOrcamento : empresa.getOrcamentoList()) {
                Empresa oldIdEmpresaOfOrcamentoListOrcamento = orcamentoListOrcamento.getIdEmpresa();
                orcamentoListOrcamento.setIdEmpresa(empresa);
                orcamentoListOrcamento = em.merge(orcamentoListOrcamento);
                if (oldIdEmpresaOfOrcamentoListOrcamento != null) {
                    oldIdEmpresaOfOrcamentoListOrcamento.getOrcamentoList().remove(orcamentoListOrcamento);
                    oldIdEmpresaOfOrcamentoListOrcamento = em.merge(oldIdEmpresaOfOrcamentoListOrcamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getIdEmpresa());
            List<Orcamento> orcamentoListOld = persistentEmpresa.getOrcamentoList();
            List<Orcamento> orcamentoListNew = empresa.getOrcamentoList();
            List<String> illegalOrphanMessages = null;
            for (Orcamento orcamentoListOldOrcamento : orcamentoListOld) {
                if (!orcamentoListNew.contains(orcamentoListOldOrcamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orcamento " + orcamentoListOldOrcamento + " since its idEmpresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Orcamento> attachedOrcamentoListNew = new ArrayList<Orcamento>();
            for (Orcamento orcamentoListNewOrcamentoToAttach : orcamentoListNew) {
                orcamentoListNewOrcamentoToAttach = em.getReference(orcamentoListNewOrcamentoToAttach.getClass(), orcamentoListNewOrcamentoToAttach.getIdOrcamento());
                attachedOrcamentoListNew.add(orcamentoListNewOrcamentoToAttach);
            }
            orcamentoListNew = attachedOrcamentoListNew;
            empresa.setOrcamentoList(orcamentoListNew);
            empresa = em.merge(empresa);
            for (Orcamento orcamentoListNewOrcamento : orcamentoListNew) {
                if (!orcamentoListOld.contains(orcamentoListNewOrcamento)) {
                    Empresa oldIdEmpresaOfOrcamentoListNewOrcamento = orcamentoListNewOrcamento.getIdEmpresa();
                    orcamentoListNewOrcamento.setIdEmpresa(empresa);
                    orcamentoListNewOrcamento = em.merge(orcamentoListNewOrcamento);
                    if (oldIdEmpresaOfOrcamentoListNewOrcamento != null && !oldIdEmpresaOfOrcamentoListNewOrcamento.equals(empresa)) {
                        oldIdEmpresaOfOrcamentoListNewOrcamento.getOrcamentoList().remove(orcamentoListNewOrcamento);
                        oldIdEmpresaOfOrcamentoListNewOrcamento = em.merge(oldIdEmpresaOfOrcamentoListNewOrcamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = empresa.getIdEmpresa();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getIdEmpresa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orcamento> orcamentoListOrphanCheck = empresa.getOrcamentoList();
            for (Orcamento orcamentoListOrphanCheckOrcamento : orcamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Orcamento " + orcamentoListOrphanCheckOrcamento + " in its orcamentoList field has a non-nullable idEmpresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
