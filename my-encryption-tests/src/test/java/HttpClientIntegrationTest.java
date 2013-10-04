import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.encryption.testing.client.HttpClient;


public class HttpClientIntegrationTest {
    private HttpClient httpClient;
    
    @Before
    public void setUp() {
        httpClient = new HttpClient();
    }
    
    // TODO: Do not assume that the server is running, post to some example webpage on the net.
    @Test
    public void canPostToSpringController() {
        // Given
        
        // When
        String response = httpClient.postToController("http://localhost:8888/my-encryption-testing/getEncryptedExample", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><xml>bla</xml>");
        // Then
        assertEquals(response, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><xml>bla</xml>");
        System.out.println(response);
    }
}
