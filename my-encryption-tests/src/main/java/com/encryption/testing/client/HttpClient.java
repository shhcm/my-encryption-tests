package com.encryption.testing.client;


import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static org.apache.commons.io.IOUtils.copy;

/*
 *
 * TODO: Generate an asymetric key pair in the controller, that can be retrieved by the client.
 * The client should then send an encrypted xml message to the controller, that only the
 * controller is able to decrypt.
 * 
 **/

public class HttpClient implements ClientInterface {
    
    private HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
    
    @Override
    public String postToController(String uri, String postRequestBody) {
        // TODO: transmit an xml entity that should be encrypted by the client.
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
    @Override
    public String getPublicKeyFromController(String uri, int securityParameter) {
        CloseableHttpClient httpClient = httpClientBuilder.build();
        
        try {
            URIBuilder uriBuilder = new URIBuilder(uri).addParameter("securityParameter", ""+securityParameter);
            HttpGet request = new HttpGet(uriBuilder.build());

            HttpResponse response = httpClient.execute(request);
            StringWriter stringWriter = new StringWriter();
            copy(response.getEntity().getContent(), stringWriter);
            return stringWriter.toString();
            
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
