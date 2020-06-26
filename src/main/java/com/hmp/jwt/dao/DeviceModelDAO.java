package com.hmp.jwt.dao;

import com.hmp.jwt.entity.DeviceModel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@ApplicationScoped
public class DeviceModelDAO {
    @Inject
    EntityManager entityManager;

    @Transactional
    public DeviceModel findOrCeateIt(String model, String manufacturer){
        try {
            TypedQuery<DeviceModel> typedQuery = entityManager.createQuery("select m from DeviceModel m where m.model= ?1 and m.manufacturer= ?2", DeviceModel.class);
            typedQuery.setParameter(1, model);
            typedQuery.setParameter(2, manufacturer);
            return typedQuery.getSingleResult();
        } catch (Exception e){
            DeviceModel deviceModel = new DeviceModel();
            deviceModel.setModel(model);
            deviceModel.setManufacturer(manufacturer);
            entityManager.persist(deviceModel);
            return deviceModel;
        }
    }
}
