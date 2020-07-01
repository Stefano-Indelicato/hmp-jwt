package com.hmp.jwt.entity;

import javax.persistence.*;

@Entity
@Table(name = "setting")
@NamedQueries({ @NamedQuery(name="selectAllSetting", query="select s from Setting s")})
public class Setting {
    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="secret_mdm")
    private String secretMDM;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecretMDM() {
        return secretMDM;
    }

    public void setSecretMDM(String secretMDM) {
        this.secretMDM = secretMDM;
    }
}
