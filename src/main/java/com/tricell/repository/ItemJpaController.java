/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tricell.repository;

import com.tricell.model.Item;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.tricell.model.Itensorc;
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
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) {
        if (item.getItensorcList() == null) {
            item.setItensorcList(new ArrayList<Itensorc>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Itensorc> attachedItensorcList = new ArrayList<Itensorc>();
            for (Itensorc itensorcListItensorcToAttach : item.getItensorcList()) {
                itensorcListItensorcToAttach = em.getReference(itensorcListItensorcToAttach.getClass(), itensorcListItensorcToAttach.getItensorcPK());
                attachedItensorcList.add(itensorcListItensorcToAttach);
            }
            item.setItensorcList(attachedItensorcList);
            em.persist(item);
            for (Itensorc itensorcListItensorc : item.getItensorcList()) {
                Item oldItemOfItensorcListItensorc = itensorcListItensorc.getItem();
                itensorcListItensorc.setItem(item);
                itensorcListItensorc = em.merge(itensorcListItensorc);
                if (oldItemOfItensorcListItensorc != null) {
                    oldItemOfItensorcListItensorc.getItensorcList().remove(itensorcListItensorc);
                    oldItemOfItensorcListItensorc = em.merge(oldItemOfItensorcListItensorc);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getIdItem());
            List<Itensorc> itensorcListOld = persistentItem.getItensorcList();
            List<Itensorc> itensorcListNew = item.getItensorcList();
            List<String> illegalOrphanMessages = null;
            for (Itensorc itensorcListOldItensorc : itensorcListOld) {
                if (!itensorcListNew.contains(itensorcListOldItensorc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Itensorc " + itensorcListOldItensorc + " since its item field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Itensorc> attachedItensorcListNew = new ArrayList<Itensorc>();
            for (Itensorc itensorcListNewItensorcToAttach : itensorcListNew) {
                itensorcListNewItensorcToAttach = em.getReference(itensorcListNewItensorcToAttach.getClass(), itensorcListNewItensorcToAttach.getItensorcPK());
                attachedItensorcListNew.add(itensorcListNewItensorcToAttach);
            }
            itensorcListNew = attachedItensorcListNew;
            item.setItensorcList(itensorcListNew);
            item = em.merge(item);
            for (Itensorc itensorcListNewItensorc : itensorcListNew) {
                if (!itensorcListOld.contains(itensorcListNewItensorc)) {
                    Item oldItemOfItensorcListNewItensorc = itensorcListNewItensorc.getItem();
                    itensorcListNewItensorc.setItem(item);
                    itensorcListNewItensorc = em.merge(itensorcListNewItensorc);
                    if (oldItemOfItensorcListNewItensorc != null && !oldItemOfItensorcListNewItensorc.equals(item)) {
                        oldItemOfItensorcListNewItensorc.getItensorcList().remove(itensorcListNewItensorc);
                        oldItemOfItensorcListNewItensorc = em.merge(oldItemOfItensorcListNewItensorc);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = item.getIdItem();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getIdItem();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Itensorc> itensorcListOrphanCheck = item.getItensorcList();
            for (Itensorc itensorcListOrphanCheckItensorc : itensorcListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Item (" + item + ") cannot be destroyed since the Itensorc " + itensorcListOrphanCheckItensorc + " in its itensorcList field has a non-nullable item field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
