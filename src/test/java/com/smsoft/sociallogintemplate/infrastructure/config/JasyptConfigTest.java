package com.smsoft.sociallogintemplate.infrastructure.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JasyptConfigTest {
    @Test
    @DisplayName("패스워드를 jasypt로 암호화")
    public void jasyptEncryptorPassword() {
        String key = "12";

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(8);   // 코어 수
        encryptor.setPassword(key);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");  // 암호화 알고리즘

        String str = "";
        String encryptStr = encryptor.encrypt(str);
        String decryptStr = encryptor.decrypt(encryptStr);

        System.out.println("암호화 된 문자열 : " + encryptStr);
        System.out.println("복호화 된 문자열 : " + decryptStr);
    }
}