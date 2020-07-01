package com.hmp.jwt.dao;

import com.hmp.jwt.entity.Device;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeviceDAO {
    @Inject
    EntityManager em;

    public Device findById(Long id){
        return em.find(Device.class, id);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void merge(Device device){
        em.merge(device);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void persist(Device device){ em.persist(device);}
}
