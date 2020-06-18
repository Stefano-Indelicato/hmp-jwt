package com.hmp.jwt.dao;

import com.hmp.jwt.entity.Worker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class WorkerDAO {
    @Inject
    EntityManager entityManager;

    public Worker findById(Integer id){
        return entityManager.find(Worker.class, id);
    }

    @Transactional
    public void merge(Worker worker){
        entityManager.merge(worker);
    }

}
