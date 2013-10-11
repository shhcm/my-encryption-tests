package com.encryption.testing.client;


import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static org.apache.commons.io.IOUtils.copy;


public class HttpClient {
    
    
    public String postToController(String uri, String postRequestBody) {
        // TODO: transmit an xml entity that should be encrypted by the client.
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = httpClientBuilder.build();
        
        HttpPost request = new HttpPost(uri);
        
        EntityBuilder entityBuilder = EntityBuilder.create();
        entityBuilder.setText(postRequestBody);
        
        Charset charset = Charset.forName("UTF-8");
        ContentType contentType = ContentType.create("application/xml", charset);
        entityBuilder.setContentType(contentType);
        
        request.setEntity(entityBuilder.build());
        
        try {
            HttpResponse response = httpClient.execute(request);
            StringWriter stringWriter = new StringWriter();
            copy(response.getEntity().getContent(), stringWriter);
            return stringWriter.toString();
            
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
