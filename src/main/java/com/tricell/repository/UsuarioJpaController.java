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
import com.tricell.model.Orcamento;
import com.tricell.model.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getOrcamentoList() == null) {
            usuario.setOrcamentoList(new ArrayList<Orcamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Orcamento> attachedOrcamentoList = new ArrayList<Orcamento>();
            for (Orcamento orcamentoListOrcamentoToAttach : usuario.getOrcamentoList()) {
                orcamentoListOrcamentoToAttach = em.getReference(orcamentoListOrcamentoToAttach.getClass(), orcamentoListOrcamentoToAttach.getIdOrcamento());
                attachedOrcamentoList.add(orcamentoListOrcamentoToAttach);
            }
            usuario.setOrcamentoList(attachedOrcamentoList);
            em.persist(usuario);
            for (Orcamento orcamentoListOrcamento : usuario.getOrcamentoList()) {
                Usuario oldIdUsuarioOfOrcamentoListOrcamento = orcamentoListOrcamento.getIdUsuario();
                orcamentoListOrcamento.setIdUsuario(usuario);
                orcamentoListOrcamento = em.merge(orcamentoListOrcamento);
                if (oldIdUsuarioOfOrcamentoListOrcamento != null) {
                    oldIdUsuarioOfOrcamentoListOrcamento.getOrcamentoList().remove(orcamentoListOrcamento);
                    oldIdUsuarioOfOrcamentoListOrcamento = em.merge(oldIdUsuarioOfOrcamentoListOrcamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            List<Orcamento> orcamentoListOld = persistentUsuario.getOrcamentoList();
            List<Orcamento> orcamentoListNew = usuario.getOrcamentoList();
            List<String> illegalOrphanMessages = null;
            for (Orcamento orcamentoListOldOrcamento : orcamentoListOld) {
                if (!orcamentoListNew.contains(orcamentoListOldOrcamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orcamento " + orcamentoListOldOrcamento + " since its idUsuario field is not nullable.");
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
            usuario.setOrcamentoList(orcamentoListNew);
            usuario = em.merge(usuario);
            for (Orcamento orcamentoListNewOrcamento : orcamentoListNew) {
                if (!orcamentoListOld.contains(orcamentoListNewOrcamento)) {
                    Usuario oldIdUsuarioOfOrcamentoListNewOrcamento = orcamentoListNewOrcamento.getIdUsuario();
                    orcamentoListNewOrcamento.setIdUsuario(usuario);
                    orcamentoListNewOrcamento = em.merge(orcamentoListNewOrcamento);
                    if (oldIdUsuarioOfOrcamentoListNewOrcamento != null && !oldIdUsuarioOfOrcamentoListNewOrcamento.equals(usuario)) {
                        oldIdUsuarioOfOrcamentoListNewOrcamento.getOrcamentoList().remove(orcamentoListNewOrcamento);
                        oldIdUsuarioOfOrcamentoListNewOrcamento = em.merge(oldIdUsuarioOfOrcamentoListNewOrcamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orcamento> orcamentoListOrphanCheck = usuario.getOrcamentoList();
            for (Orcamento orcamentoListOrphanCheckOrcamento : orcamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Orcamento " + orcamentoListOrphanCheckOrcamento + " in its orcamentoList field has a non-nullable idUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
