package com.hmp.jwt.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @SequenceGenerator(name="deviceSequence", sequenceName = "devices_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="deviceSequence")
    private Long id;


    @Column(name="secret")
    private String secret;

    @OneToOne
    @JoinColumn(name = "workerId", nullable = true)
    private Worker worker;

    @Column(name="language")
    private String language;

    @Column(name="os_name")
    private String osName;

    @Column(name="os_version")
    private String osVersion;


    @OneToOne
    @JoinColumn(name = "id_device_model", nullable = true)
    private DeviceModel deviceModel;

    @Column(name = "device_model")
    private String model;

    @Column(name = "device_manufacturer")
    private  String manufacturer;

    @Column(name = "updatedAt")
    private LocalDateTime lastModifiedDate;

    @Column(name = "createdAt")
    private LocalDateTime insertDate;


    @PrePersist
    public void updateDates() {

        LocalDateTime now = LocalDateTime.now();
        setInsertDate(now);
        setLastModifiedDate(now);
    }

    @PreUpdate
    public void updateLastModifiedDate() {
        setLastModifiedDate(LocalDateTime.now());
    }



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

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Long getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setInsertDate(LocalDateTime insertDate) {
        this.insertDate = insertDate;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
