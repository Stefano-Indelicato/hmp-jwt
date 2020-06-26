package com.hmp.jwt.dao;

import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.Worker;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeviceDAO {
    @Inject
    EntityManager entityManager;

    public Device findById(Integer id){
        return entityManager.find(Device.class, id);
    }

    @Transactional
    public void merge(Device device){
        entityManager.merge(device);
    }
}
