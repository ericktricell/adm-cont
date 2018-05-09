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
import com.tricell.model.Empresa;
import com.tricell.model.Cliente;
import com.tricell.model.Usuario;
import com.tricell.model.Centrocusto;
import java.util.ArrayList;
import java.util.List;
import com.tricell.model.Itensorc;
import com.tricell.model.Orcamento;
import com.tricell.repository.exceptions.IllegalOrphanException;
import com.tricell.repository.exceptions.NonexistentEntityException;
import com.tricell.repository.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Eu
 */
public class OrcamentoJpaController implements Serializable {

    public OrcamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orcamento orcamento) throws PreexistingEntityException, Exception {
        if (orcamento.getCentrocustoList() == null) {
            orcamento.setCentrocustoList(new ArrayList<Centrocusto>());
        }
        if (orcamento.getItensorcList() == null) {
            orcamento.setItensorcList(new ArrayList<Itensorc>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa idEmpresa = orcamento.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa = em.getReference(idEmpresa.getClass(), idEmpresa.getIdEmpresa());
                orcamento.setIdEmpresa(idEmpresa);
            }
            Cliente idCliente = orcamento.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                orcamento.setIdCliente(idCliente);
            }
            Usuario idUsuario = orcamento.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                orcamento.setIdUsuario(idUsuario);
            }
            List<Centrocusto> attachedCentrocustoList = new ArrayList<Centrocusto>();
            for (Centrocusto centrocustoListCentrocustoToAttach : orcamento.getCentrocustoList()) {
                centrocustoListCentrocustoToAttach = em.getReference(centrocustoListCentrocustoToAttach.getClass(), centrocustoListCentrocustoToAttach.getIdCentroCusto());
                attachedCentrocustoList.add(centrocustoListCentrocustoToAttach);
            }
            orcamento.setCentrocustoList(attachedCentrocustoList);
            List<Itensorc> attachedItensorcList = new ArrayList<Itensorc>();
            for (Itensorc itensorcListItensorcToAttach : orcamento.getItensorcList()) {
                itensorcListItensorcToAttach = em.getReference(itensorcListItensorcToAttach.getClass(), itensorcListItensorcToAttach.getItensorcPK());
                attachedItensorcList.add(itensorcListItensorcToAttach);
            }
            orcamento.setItensorcList(attachedItensorcList);
            em.persist(orcamento);
            if (idEmpresa != null) {
                idEmpresa.getOrcamentoList().add(orcamento);
                idEmpresa = em.merge(idEmpresa);
            }
            if (idCliente != null) {
                idCliente.getOrcamentoList().add(orcamento);
                idCliente = em.merge(idCliente);
            }
            if (idUsuario != null) {
                idUsuario.getOrcamentoList().add(orcamento);
                idUsuario = em.merge(idUsuario);
            }
            for (Centrocusto centrocustoListCentrocusto : orcamento.getCentrocustoList()) {
                Orcamento oldIdOrcamentoOfCentrocustoListCentrocusto = centrocustoListCentrocusto.getIdOrcamento();
                centrocustoListCentrocusto.setIdOrcamento(orcamento);
                centrocustoListCentrocusto = em.merge(centrocustoListCentrocusto);
                if (oldIdOrcamentoOfCentrocustoListCentrocusto != null) {
                    oldIdOrcamentoOfCentrocustoListCentrocusto.getCentrocustoList().remove(centrocustoListCentrocusto);
                    oldIdOrcamentoOfCentrocustoListCentrocusto = em.merge(oldIdOrcamentoOfCentrocustoListCentrocusto);
                }
            }
            for (Itensorc itensorcListItensorc : orcamento.getItensorcList()) {
                Orcamento oldOrcamentoOfItensorcListItensorc = itensorcListItensorc.getOrcamento();
                itensorcListItensorc.setOrcamento(orcamento);
                itensorcListItensorc = em.merge(itensorcListItensorc);
                if (oldOrcamentoOfItensorcListItensorc != null) {
                    oldOrcamentoOfItensorcListItensorc.getItensorcList().remove(itensorcListItensorc);
                    oldOrcamentoOfItensorcListItensorc = em.merge(oldOrcamentoOfItensorcListItensorc);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrcamento(orcamento.getIdOrcamento()) != null) {
                throw new PreexistingEntityException("Orcamento " + orcamento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orcamento orcamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orcamento persistentOrcamento = em.find(Orcamento.class, orcamento.getIdOrcamento());
            Empresa idEmpresaOld = persistentOrcamento.getIdEmpresa();
            Empresa idEmpresaNew = orcamento.getIdEmpresa();
            Cliente idClienteOld = persistentOrcamento.getIdCliente();
            Cliente idClienteNew = orcamento.getIdCliente();
            Usuario idUsuarioOld = persistentOrcamento.getIdUsuario();
            Usuario idUsuarioNew = orcamento.getIdUsuario();
            List<Centrocusto> centrocustoListOld = persistentOrcamento.getCentrocustoList();
            List<Centrocusto> centrocustoListNew = orcamento.getCentrocustoList();
            List<Itensorc> itensorcListOld = persistentOrcamento.getItensorcList();
            List<Itensorc> itensorcListNew = orcamento.getItensorcList();
            List<String> illegalOrphanMessages = null;
            for (Centrocusto centrocustoListOldCentrocusto : centrocustoListOld) {
                if (!centrocustoListNew.contains(centrocustoListOldCentrocusto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Centrocusto " + centrocustoListOldCentrocusto + " since its idOrcamento field is not nullable.");
                }
            }
            for (Itensorc itensorcListOldItensorc : itensorcListOld) {
                if (!itensorcListNew.contains(itensorcListOldItensorc)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Itensorc " + itensorcListOldItensorc + " since its orcamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEmpresaNew != null) {
                idEmpresaNew = em.getReference(idEmpresaNew.getClass(), idEmpresaNew.getIdEmpresa());
                orcamento.setIdEmpresa(idEmpresaNew);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                orcamento.setIdCliente(idClienteNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                orcamento.setIdUsuario(idUsuarioNew);
            }
            List<Centrocusto> attachedCentrocustoListNew = new ArrayList<Centrocusto>();
            for (Centrocusto centrocustoListNewCentrocustoToAttach : centrocustoListNew) {
                centrocustoListNewCentrocustoToAttach = em.getReference(centrocustoListNewCentrocustoToAttach.getClass(), centrocustoListNewCentrocustoToAttach.getIdCentroCusto());
                attachedCentrocustoListNew.add(centrocustoListNewCentrocustoToAttach);
            }
            centrocustoListNew = attachedCentrocustoListNew;
            orcamento.setCentrocustoList(centrocustoListNew);
            List<Itensorc> attachedItensorcListNew = new ArrayList<Itensorc>();
            for (Itensorc itensorcListNewItensorcToAttach : itensorcListNew) {
                itensorcListNewItensorcToAttach = em.getReference(itensorcListNewItensorcToAttach.getClass(), itensorcListNewItensorcToAttach.getItensorcPK());
                attachedItensorcListNew.add(itensorcListNewItensorcToAttach);
            }
            itensorcListNew = attachedItensorcListNew;
            orcamento.setItensorcList(itensorcListNew);
            orcamento = em.merge(orcamento);
            if (idEmpresaOld != null && !idEmpresaOld.equals(idEmpresaNew)) {
                idEmpresaOld.getOrcamentoList().remove(orcamento);
                idEmpresaOld = em.merge(idEmpresaOld);
            }
            if (idEmpresaNew != null && !idEmpresaNew.equals(idEmpresaOld)) {
                idEmpresaNew.getOrcamentoList().add(orcamento);
                idEmpresaNew = em.merge(idEmpresaNew);
            }
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getOrcamentoList().remove(orcamento);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getOrcamentoList().add(orcamento);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getOrcamentoList().remove(orcamento);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getOrcamentoList().add(orcamento);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Centrocusto centrocustoListNewCentrocusto : centrocustoListNew) {
                if (!centrocustoListOld.contains(centrocustoListNewCentrocusto)) {
                    Orcamento oldIdOrcamentoOfCentrocustoListNewCentrocusto = centrocustoListNewCentrocusto.getIdOrcamento();
                    centrocustoListNewCentrocusto.setIdOrcamento(orcamento);
                    centrocustoListNewCentrocusto = em.merge(centrocustoListNewCentrocusto);
                    if (oldIdOrcamentoOfCentrocustoListNewCentrocusto != null && !oldIdOrcamentoOfCentrocustoListNewCentrocusto.equals(orcamento)) {
                        oldIdOrcamentoOfCentrocustoListNewCentrocusto.getCentrocustoList().remove(centrocustoListNewCentrocusto);
                        oldIdOrcamentoOfCentrocustoListNewCentrocusto = em.merge(oldIdOrcamentoOfCentrocustoListNewCentrocusto);
                    }
                }
            }
            for (Itensorc itensorcListNewItensorc : itensorcListNew) {
                if (!itensorcListOld.contains(itensorcListNewItensorc)) {
                    Orcamento oldOrcamentoOfItensorcListNewItensorc = itensorcListNewItensorc.getOrcamento();
                    itensorcListNewItensorc.setOrcamento(orcamento);
                    itensorcListNewItensorc = em.merge(itensorcListNewItensorc);
                    if (oldOrcamentoOfItensorcListNewItensorc != null && !oldOrcamentoOfItensorcListNewItensorc.equals(orcamento)) {
                        oldOrcamentoOfItensorcListNewItensorc.getItensorcList().remove(itensorcListNewItensorc);
                        oldOrcamentoOfItensorcListNewItensorc = em.merge(oldOrcamentoOfItensorcListNewItensorc);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = orcamento.getIdOrcamento();
                if (findOrcamento(id) == null) {
                    throw new NonexistentEntityException("The orcamento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orcamento orcamento;
            try {
                orcamento = em.getReference(Orcamento.class, id);
                orcamento.getIdOrcamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orcamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Centrocusto> centrocustoListOrphanCheck = orcamento.getCentrocustoList();
            for (Centrocusto centrocustoListOrphanCheckCentrocusto : centrocustoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orcamento (" + orcamento + ") cannot be destroyed since the Centrocusto " + centrocustoListOrphanCheckCentrocusto + " in its centrocustoList field has a non-nullable idOrcamento field.");
            }
            List<Itensorc> itensorcListOrphanCheck = orcamento.getItensorcList();
            for (Itensorc itensorcListOrphanCheckItensorc : itensorcListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orcamento (" + orcamento + ") cannot be destroyed since the Itensorc " + itensorcListOrphanCheckItensorc + " in its itensorcList field has a non-nullable orcamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa idEmpresa = orcamento.getIdEmpresa();
            if (idEmpresa != null) {
                idEmpresa.getOrcamentoList().remove(orcamento);
                idEmpresa = em.merge(idEmpresa);
            }
            Cliente idCliente = orcamento.getIdCliente();
            if (idCliente != null) {
                idCliente.getOrcamentoList().remove(orcamento);
                idCliente = em.merge(idCliente);
            }
            Usuario idUsuario = orcamento.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getOrcamentoList().remove(orcamento);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(orcamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orcamento> findOrcamentoEntities() {
        return findOrcamentoEntities(true, -1, -1);
    }

    public List<Orcamento> findOrcamentoEntities(int maxResults, int firstResult) {
        return findOrcamentoEntities(false, maxResults, firstResult);
    }

    private List<Orcamento> findOrcamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orcamento.class));
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

    public Orcamento findOrcamento(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orcamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrcamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orcamento> rt = cq.from(Orcamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
