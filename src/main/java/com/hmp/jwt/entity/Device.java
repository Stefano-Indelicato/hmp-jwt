package com.hmp.jwt.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    private Integer id;


    @Column(name="secret")
    private String secret;

    @Column(name="workerId")
    private Integer workerId;

    @Column(name="language")
    private String language;

    @Column(name="os_name")
    private String osName;

    @Column(name="os_version")
    private String osVersion;


    @OneToOne
    @JoinColumn(name = "id_device_model", nullable = true)
    @MapsId
    private DeviceModel deviceModel;

    @Column(name = "device_model")
    private String model;

    @Column(name = "device_manufacturer")
    private  String manufacturer;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }


    public void setSecret(String secret) {
        this.secret = secret;
    }


    public Integer getWorkerId() {
        return workerId;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public void setUpdatedAt(LocalDateTime lastModifiedDate) {
        this.updatedAt = lastModifiedDate;
    }
}
