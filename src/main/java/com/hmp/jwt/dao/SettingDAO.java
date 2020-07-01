package com.hmp.jwt.dao;

import com.hmp.jwt.entity.Setting;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class SettingDAO {
    @Inject
    EntityManager em;

    public Setting getSetting() throws Exception {
        try{
            return em.createNamedQuery("selectAllSetting", Setting.class)
                    .getResultList().get(0);
        } catch (Exception e){
            throw new Exception("No settings in db!", e);
        }
    }
}
