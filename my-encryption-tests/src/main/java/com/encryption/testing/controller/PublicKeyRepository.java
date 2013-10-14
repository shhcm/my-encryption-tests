package com.encryption.testing.controller;

import java.security.PublicKey;

public interface PublicKeyRepository {
    /**
     * Return a cached public key with security parameter securityParameter.
     * Generate a new key pair if no key is cached for this security parameter.
     *
     * @param securityParameter key size in bits
     * @return the public key
     */
    public PublicKey getPublicKey(int securityParameter);
}
