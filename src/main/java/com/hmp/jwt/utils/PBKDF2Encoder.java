package com.hmp.jwt.utils;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ApplicationScoped
public class PBKDF2Encoder {

    @ConfigProperty(name = "com.hmp.jwt.password.secret")
    String secret;
    @ConfigProperty(name = "com.hmp.jwt.password.iteration")
    Integer iteration;
    @ConfigProperty(name = "com.hmp.jwt.password.keylength")
    Integer keylength;

    /**
      *
     * @param cs test
     * @return encoded text
     */
    public String encode(String str) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(str.toCharArray(), secret.getBytes(), iteration, keylength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }
}