package com.encryption.testing.client;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.xml.security.encryption.XMLCipher;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;











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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            encInputStream2EncOutputStream( response.getEntity().getContent(),
                                            "utf-8",
                                            byteArrayOutputStream,
                                            "utf-8");
            return new String(byteArrayOutputStream.toByteArray());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public String getPublicKeyFromController(String uri, int securityParameter) {
        CloseableHttpClient httpClient = httpClientBuilder.build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        
        try {
            URIBuilder uriBuilder = new URIBuilder(uri).addParameter("securityParameter", "" + securityParameter);
            HttpGet request = new HttpGet(uriBuilder.build());
            HttpResponse response = httpClient.execute(request);
            
            // Generic method to write from some InputStream to some OutputStream while specifying input and output encoding manually.
            encInputStream2EncOutputStream( response.getEntity().getContent(),
                                            "utf-8",
                                            byteArrayOutputStream,
                                            "utf-8");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        
        return new String(byteArrayOutputStream.toByteArray());
    }
    
    static void prepareXmlEncryptionMessage(byte[] publicKey, Document message ) {
        
        /*PublicKey pubKey = keyFactory.generatePublic(publicKey);
        PublicKey keyEncryptionKey = new PublicKey();
        XMLCipher xmlCipher = XMLCipher.getInstance();
        xmlCipher.init(WRAP_MODE,);
        EncryptedData encryptedDataElement = xmlCipher.*/
        try {
            // Create a key object for the rsa key encryption key.
            // Key information (algorithm, encoding format) should be retrieved from server as well, instead of hardcodig this here.
            X509EncodedKeySpec keySpecKeyEncryption = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            PublicKey keyEncryptionKey = keyFactory.generatePublic(keySpecKeyEncryption);
            // TODO: generate the enrypted XML document with this public Key
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    static void encInputStream2EncOutputStream( InputStream inputStream,
                                                String inputEncoding,
                                                OutputStream outputStream,
                                                String outputEncoding) throws IOException  {
        // Generic method to write from some InputStream to some OutputStream while specifying input and output encoding manually.
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, inputEncoding);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, outputEncoding);
        copy(inputStreamReader, outputStreamWriter);
        inputStreamReader.close();
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }
}
