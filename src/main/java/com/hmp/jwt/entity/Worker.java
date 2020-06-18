package com.hmp.jwt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    private Integer id;

    @Column(name="expirein")
    private LocalDateTime expirein;

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    @Column(name ="enabled")
    private Boolean isEnabled;

    public Integer getId() {
        return id;
    }

    public LocalDateTime getExpirein() {
        return expirein;
    }
}
