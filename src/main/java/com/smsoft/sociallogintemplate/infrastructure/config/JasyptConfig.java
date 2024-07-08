package com.smsoft.sociallogintemplate.infrastructure.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableEncryptableProperties
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String key;

    @Bean
    public PooledPBEStringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(8);
        encryptor.setPassword(key);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");

        return encryptor;
    }
}
