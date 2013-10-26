package com.encryption.testing.controller;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class KeyPairRepositoryImpl implements KeyPairRepository{
    private HashMap<Integer, KeyPair> asymetricKeyMap = new HashMap<Integer, KeyPair>();
    
    @Override
    public PublicKey getPublicKey(int securityParameter) {
        return getKeyPair(securityParameter).getPublic();
    }
    
    @Override
    public PrivateKey getPrivateKey(int securityParameter) {
        return getKeyPair(securityParameter).getPrivate();
    }
    
    private KeyPair getKeyPair(int securityParameter) {
        if(!asymetricKeyMap.containsKey(securityParameter)) {
            try {
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
                keyPairGen.initialize(securityParameter);
                KeyPair asymetricKey = keyPairGen.generateKeyPair();
                asymetricKeyMap.put(securityParameter, asymetricKey);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return asymetricKeyMap.get(securityParameter);
    }
}
