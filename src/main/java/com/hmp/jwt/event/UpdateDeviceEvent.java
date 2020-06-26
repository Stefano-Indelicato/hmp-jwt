package com.hmp.jwt.event;

import com.hmp.jwt.entity.Device;

public class UpdateDeviceEvent {
    private Device device;

    public UpdateDeviceEvent(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
