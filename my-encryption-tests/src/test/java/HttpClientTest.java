import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.encryption.testing.client.HttpClient;


public class HttpClientTest {
    private HttpClient httpClient;
    
    @Before
    public void setUp() {
        httpClient = new HttpClient();
    }
    
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
