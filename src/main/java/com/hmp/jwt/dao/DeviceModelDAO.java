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
    EntityManager em;

    @Transactional
    public DeviceModel findOrCeateIt(String model, String manufacturer){
        try {
            return em.createNamedQuery("selectDeviceModelFromManufacturer", DeviceModel.class)
                    .setParameter("model", model)
                    .setParameter("manufacturer", manufacturer)
                    .getSingleResult();
        } catch (Exception e){
            DeviceModel deviceModel = new DeviceModel();
            deviceModel.setModel(model);
            deviceModel.setManufacturer(manufacturer);
            em.persist(deviceModel);
            return deviceModel;
        }
    }
}
