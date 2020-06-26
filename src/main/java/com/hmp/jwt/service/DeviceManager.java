package com.hmp.jwt.service;

import com.hmp.jwt.dao.DeviceDAO;
import com.hmp.jwt.dao.DeviceModelDAO;
import com.hmp.jwt.entity.Device;
import com.hmp.jwt.entity.DeviceModel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.net.http.HttpHeaders;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequestScoped
public class DeviceManager {
    @Inject
    DeviceDAO deviceDAO;

    @Inject
    DeviceModelDAO deviceModelDAO;

    public void updateDevice(Device device, HttpHeaders httpHeaders){
        if (!httpHeaders.firstValue("x-device-name").isEmpty()
                && !httpHeaders.firstValue("x-device-manufacturer").isEmpty()) {
            DeviceModel deviceModel = deviceModelDAO.findOrCeateIt(httpHeaders.firstValue("x-device-name").get(), httpHeaders.firstValue("x-device-manufacturer").get());
            device.setDeviceModel(deviceModel);
            device.setModel(deviceModel.getModel());
            device.setManufacturer(deviceModel.getManufacturer());
            if (!httpHeaders.firstValue("x-client-os").isEmpty() && !httpHeaders.firstValue("x-client-version").isEmpty()) {
                device.setOsName(httpHeaders.firstValue("x-client-os").get());
                device.setOsVersion(httpHeaders.firstValue("x-client-os").get());

            }
            if (!httpHeaders.firstValue("Accept-Language").isEmpty()) {
                 device.setLanguage( httpHeaders.firstValue("Accept-Language").get()); }

            device.setUpdatedAt(LocalDateTime.now());

            deviceDAO.merge(device);
        }
    }

}
