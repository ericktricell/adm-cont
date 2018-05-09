/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.repository;

import com.tricell.model.Cliente;
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
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getOrcamentoList() == null) {
            cliente.setOrcamentoList(new ArrayList<Orcamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Orcamento> attachedOrcamentoList = new ArrayList<Orcamento>();
            for (Orcamento orcamentoListOrcamentoToAttach : cliente.getOrcamentoList()) {
                orcamentoListOrcamentoToAttach = em.getReference(orcamentoListOrcamentoToAttach.getClass(), orcamentoListOrcamentoToAttach.getIdOrcamento());
                attachedOrcamentoList.add(orcamentoListOrcamentoToAttach);
            }
            cliente.setOrcamentoList(attachedOrcamentoList);
            em.persist(cliente);
            for (Orcamento orcamentoListOrcamento : cliente.getOrcamentoList()) {
                Cliente oldIdClienteOfOrcamentoListOrcamento = orcamentoListOrcamento.getIdCliente();
                orcamentoListOrcamento.setIdCliente(cliente);
                orcamentoListOrcamento = em.merge(orcamentoListOrcamento);
                if (oldIdClienteOfOrcamentoListOrcamento != null) {
                    oldIdClienteOfOrcamentoListOrcamento.getOrcamentoList().remove(orcamentoListOrcamento);
                    oldIdClienteOfOrcamentoListOrcamento = em.merge(oldIdClienteOfOrcamentoListOrcamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getIdCliente());
            List<Orcamento> orcamentoListOld = persistentCliente.getOrcamentoList();
            List<Orcamento> orcamentoListNew = cliente.getOrcamentoList();
            List<String> illegalOrphanMessages = null;
            for (Orcamento orcamentoListOldOrcamento : orcamentoListOld) {
                if (!orcamentoListNew.contains(orcamentoListOldOrcamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orcamento " + orcamentoListOldOrcamento + " since its idCliente field is not nullable.");
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
            cliente.setOrcamentoList(orcamentoListNew);
            cliente = em.merge(cliente);
            for (Orcamento orcamentoListNewOrcamento : orcamentoListNew) {
                if (!orcamentoListOld.contains(orcamentoListNewOrcamento)) {
                    Cliente oldIdClienteOfOrcamentoListNewOrcamento = orcamentoListNewOrcamento.getIdCliente();
                    orcamentoListNewOrcamento.setIdCliente(cliente);
                    orcamentoListNewOrcamento = em.merge(orcamentoListNewOrcamento);
                    if (oldIdClienteOfOrcamentoListNewOrcamento != null && !oldIdClienteOfOrcamentoListNewOrcamento.equals(cliente)) {
                        oldIdClienteOfOrcamentoListNewOrcamento.getOrcamentoList().remove(orcamentoListNewOrcamento);
                        oldIdClienteOfOrcamentoListNewOrcamento = em.merge(oldIdClienteOfOrcamentoListNewOrcamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cliente.getIdCliente();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getIdCliente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orcamento> orcamentoListOrphanCheck = cliente.getOrcamentoList();
            for (Orcamento orcamentoListOrphanCheckOrcamento : orcamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Orcamento " + orcamentoListOrphanCheckOrcamento + " in its orcamentoList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
