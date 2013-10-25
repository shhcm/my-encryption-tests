import static org.junit.Assert.*;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.Test;

import com.encryption.testing.client.HttpClient;

/*
 * Test requires jetty to be running.
 * TODO: add test for retrieving the public key.
 **/

public class HttpClientITCase {
    private HttpClient httpClient;
    
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
        System.out.println(response);
    }
    
    @Test
    public void canRetrievePublicKeyFromController() {
        //Given
        
        // When
        String response = httpClient.getPublicKeyFromController("http://localhost:8888/my-encryption-testing/getPublicKey", 1024);
        // Then
        System.out.println("Public Key (Base64): " + response);
        // TODO: improve verifying that an actual public key is returned.
        assertNotNull(Base64.decode(response));
        assertTrue(Base64.decode(response).length > 150);
    }
}
