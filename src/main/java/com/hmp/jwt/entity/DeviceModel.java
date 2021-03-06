package com.hmp.jwt.entity;

import javax.persistence.*;

@Entity
@Table(name = "device_model", uniqueConstraints = @UniqueConstraint(columnNames = {"model", "manufacturer"}) )
@NamedQueries({ @NamedQuery(name="selectDeviceModelFromManufacturer", query="select m from DeviceModel m where m.model= :model and m.manufacturer= :manufacturer")})
public class DeviceModel {
    @Id
    @SequenceGenerator(name="deviceModelSequence", sequenceName = "device_model_id_seq", allocationSize = 1, initialValue = 1)
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

