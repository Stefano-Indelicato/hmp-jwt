package com.hmp.jwt.service;

import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.DeviceModelDAO;
import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.DeviceModel;
import com.hmp.jwt.event.UpdateDevice;
import com.hmp.jwt.event.UpdateDeviceEvent;
import com.hmp.jwt.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
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

    @Transactional(Transactional.TxType.REQUIRED)
    public void updateDevice(@Observes @UpdateDevice UpdateDeviceEvent updateDeviceEvent){
        Device device = updateDeviceEvent.getDevice();
        try {
            if (StringUtils.isNotEmpty(device.getModel()) && StringUtils.isNotEmpty(device.getManufacturer())) {
                DeviceModel deviceModel = deviceModelDAO.findOrCeateIt(device.getModel(), device.getManufacturer());
                device.setDeviceModel(deviceModel);
            }
            device.setUpdatedAt(LocalDateTime.now());
            deviceDAO.merge(device);
        }  catch(Exception e){
            LOG.error("Error updating Device", e);
        }
    }

}
