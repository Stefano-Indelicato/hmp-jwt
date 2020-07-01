package com.hmp.jwt.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workers")
@NamedQueries({ @NamedQuery(name="selectByEmail", query="select w from Worker w where w.email= :email")})
public class Worker {

    @Id
    private Integer id;

    @Column(name="expirein")
    private LocalDateTime expirein;

    @Column(name ="enabled")
    private Boolean isEnabled;

    @Column(name ="email")
    private String email;

    public Integer getId() {
        return id;
    }

    public LocalDateTime getExpirein() {
        return expirein;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

}
