package com.encryption.testing.client;

import org.w3c.dom.Document;

public interface ClientInterface {
    
    public String postToController(String uri, String postRequestBody);

    public String getPublicKeyFromController(String uri, int securityParameter);
    
    public String postEncryptedXml(String uri, Document document);
}
