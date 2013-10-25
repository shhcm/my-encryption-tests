import static org.junit.Assert.*;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.encryption.testing.client.HttpClient;

import static org.hamcrest.Matchers.equalToIgnoringCase;


public class HttpClientITCase {
    private HttpClient httpClient;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        java.security.Security.addProvider(new BouncyCastleProvider());
        org.apache.xml.security.Init.init();
    }
    
    @Before
    public void setUp() {
        httpClient = new HttpClient();
    }
    
    @Test
    public void canPostToSpringController() {
        // Given
        
        // When
        String response = httpClient.postToController("http://localhost:8888/my-encryption-testing/getExample", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><xml>bla</xml>");
        // Then
        assertEquals(response, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><xml>bla</xml>");
    }
    
    @Test
    public void canRetrievePublicKeyFromController() {
        //Given
        
        // When
        String response = httpClient.getPublicKeyFromController("http://localhost:8888/my-encryption-testing/getPublicKey", 1024);
        X509EncodedKeySpec keySpecKeyEncryption = new X509EncodedKeySpec(Base64.decode(response));
        PublicKey keyEncryptionKey;
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("RSA", "BC");
            keyEncryptionKey = keyFactory.generatePublic(keySpecKeyEncryption);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        // Then
        assertNotNull(keyEncryptionKey);
        assertThat(keyEncryptionKey.getAlgorithm(), equalToIgnoringCase("RSA"));
        assertThat(keyEncryptionKey.getFormat(), equalToIgnoringCase("X.509"));
    }
}
