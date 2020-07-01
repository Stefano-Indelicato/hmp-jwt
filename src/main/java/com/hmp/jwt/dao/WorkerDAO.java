package com.hmp.jwt.dao;

import com.hmp.jwt.entity.Worker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class WorkerDAO {
    @Inject
    EntityManager em;

    public Worker findById(Integer id){
        return em.find(Worker.class, id);
    }

    public Worker findByEmail(String email){
        return em.createNamedQuery("selectByEmail", Worker.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Transactional
    public void merge(Worker worker){
        em.merge(worker);
    }



}
