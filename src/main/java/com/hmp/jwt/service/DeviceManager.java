package com.hmp.jwt.service;

import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.DeviceModelDAO;
import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.DeviceModel;
import com.hmp.jwt.utils.StringUtils;
import io.quarkus.vertx.ConsumeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class DeviceManager {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Inject
    DeviceDAO deviceDAO;

    @Inject
    DeviceModelDAO deviceModelDAO;

    @ConsumeEvent(value = "updateDevice", blocking = true)
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateDevice(Device device){
        try {
            if (StringUtils.isNotEmpty(device.getModel()) && StringUtils.isNotEmpty(device.getManufacturer())) {
                DeviceModel deviceModel = deviceModelDAO.findOrCeateIt(device.getModel(), device.getManufacturer());
                device.setDeviceModel(deviceModel);
            }
            deviceDAO.merge(device);
        }  catch(Exception e){
            LOG.error("Error updating Device", e);
        }
    }

}
