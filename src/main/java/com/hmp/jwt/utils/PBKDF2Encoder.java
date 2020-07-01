package com.hmp.jwt.utils;

import com.hmp.jwt.configuration.manager.GlobalConfigurationManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ApplicationScoped
public class PBKDF2Encoder {

    @Inject
    GlobalConfigurationManager globalConfigurationManager;

    @ConfigProperty(name = "com.hmp.jwt.password.iteration")
    Integer iteration;
    @ConfigProperty(name = "com.hmp.jwt.password.keylength")
    Integer keylength;

    /**
      *
     * @param str test
     * @return encoded text
     */
    public String encode(String str) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(str.toCharArray(), globalConfigurationManager.getSecretMDM().getBytes(), iteration, keylength))
                    .getEncoded();
            return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }
}