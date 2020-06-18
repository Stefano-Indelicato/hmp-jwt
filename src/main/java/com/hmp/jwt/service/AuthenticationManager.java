package com.hmp.jwt.service;

import com.hmp.jwt.enums.Role;
import com.hmp.jwt.utility.PBKDF2Encoder;
import com.hmp.jwt.utility.TokenUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@RequestScoped
public class AuthenticationManager {

    @Inject
    PBKDF2Encoder pbkdf2Encoder;

    @ConfigProperty(name = "com.hmp.jwt.duration") public Long duration;
    @ConfigProperty(name = "mp.jwt.verify.issuer") public String issuer;

    public String generateSecret()
    {
        Random random = new Random();
        return pbkdf2Encoder.encode(Long.toString(random.nextLong()));
    }

    public String generateToken(String id, String secret) throws Exception {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        return TokenUtils.generateToken(id, secret, roles, duration, issuer);
    }

}
