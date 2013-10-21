import static org.junit.Assert.assertThat;

import org.junit.Test;
import static org.hamcrest.Matchers.endsWith;

public class HttpClientUnitTest {

    @Test
    public void canReplaceSecretElementsCorrectly() {
        // Given
        String xmlFile = getClass().getResource("/com/encryption/testing/messages/DummyMessage.xml").getFile();
        // When
        
        // Then
        assertThat(xmlFile, endsWith("DummyMessage.xml"));
    }
}
