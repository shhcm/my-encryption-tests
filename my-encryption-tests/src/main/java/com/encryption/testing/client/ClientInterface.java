package com.encryption.testing.client;

public interface ClientInterface {
    
    public String postToController(String uri, String postRequestBody);

    public String getPublicKeyFromController(String uri, int securityParameter);
}
