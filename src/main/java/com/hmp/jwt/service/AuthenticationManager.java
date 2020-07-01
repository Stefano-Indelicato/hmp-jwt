package com.hmp.jwt.service;

import com.hmp.jwt.dao.WorkerDAO;
import com.hmp.jwt.entity.Worker;
import com.hmp.jwt.enums.Role;
import com.hmp.jwt.utils.PBKDF2Encoder;
import com.hmp.jwt.utils.TokenUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@RequestScoped
public class AuthenticationManager {

    @Inject
    PBKDF2Encoder pbkdf2Encoder;

    @Inject
    WorkerDAO workerDAO;


    @ConfigProperty(name = "com.hmp.jwt.duration") public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer") public String issuer;

    public String generateSecret()
    {
        return pbkdf2Encoder.encode(UUID.randomUUID().toString());
    }

    public String generateToken(String id, String secret) throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        return TokenUtils.generateToken(id, secret, roles, duration, issuer);
    }

    public boolean istWorkerExpired(Worker worker){
        LocalDateTime expirein = worker.getExpirein();
        boolean expired = false;
        if (expirein != null && expirein.compareTo(LocalDateTime.now()) < 0) {
            worker.setEnabled(expired);
            workerDAO.merge(worker);
            expired = true;
        }
        return expired;
    }

}
