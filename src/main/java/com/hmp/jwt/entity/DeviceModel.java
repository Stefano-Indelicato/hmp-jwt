package com.hmp.jwt.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_model", uniqueConstraints = @UniqueConstraint(columnNames = {"model", "manufacturer"}) )
public class DeviceModel {
    @Id
    @SequenceGenerator(name="deviceModelSequence", sequenceName = "deviceModelId_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="deviceModelSequence")
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "manufacturer")
    private  String manufacturer;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }



}

