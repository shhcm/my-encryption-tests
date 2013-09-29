
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JunitMockitoTest {

    private Object testObject;
    
    @Before
    public void setUp() {
        this.testObject = mock(Object.class);
    }
    
    @Test
    public void trivialTest() {
        // Given
        String output = "XML Encryption";
        when(this.testObject.toString()).thenReturn(output);
        
        // When
        String result = this.testObject.toString();
        
        // Then
        //verify(testObject,times(1)).toString();
        assertEquals(output, result);
    }
}
