package com.encryption.testing.controller;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Return a cached public or private key with security parameter securityParameter.
 * Generate a new key pair if no key is cached for this security parameter.
 */
public interface KeyPairRepository {
    
    public PublicKey getPublicKey(int securityParameter);
    
    public PrivateKey getPrivateKey(int securityParameter);
}
