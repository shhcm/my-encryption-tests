import static org.junit.Assert.*;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.encryption.testing.client.HttpClient;
import com.encryption.testing.helpers.XMLEncryptionHelper;

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
    
    @Test
    public void controllerCanDecryptWithPrivateKey() {
        // Given
        String xmlFile = getClass().getResource("/com/encryption/testing/messages/DummyMessage.xml").getFile();
        Document documentToEncrypt = XMLEncryptionHelper.loadXMLFromFile(xmlFile);
        
        int securityParameter = Integer.parseInt(
                                        ((Element)documentToEncrypt.getElementsByTagName("security-parameter").item(0)).getAttribute("value"));
        String secretTextNode = ((Element)documentToEncrypt.getElementsByTagName("encrypted-part").item(0)).getTextContent();
        String secretAttribute = ((Element)documentToEncrypt.getElementsByTagName("encrypted-part").item(0)).getAttribute("addidtional-secret");
        
        String publicKeyBase64 = httpClient.getPublicKeyFromController("http://localhost:8888/my-encryption-testing/getPublicKey", securityParameter);
        Document encryptedDoc = XMLEncryptionHelper.encrytUsingRSA(Base64.decode(publicKeyBase64), documentToEncrypt);
        
        // When
        String response = httpClient.postEncryptedXml("http://localhost:8888/my-encryption-testing/decryptEncrypted", encryptedDoc);
        Document decryptedDocument = XMLEncryptionHelper.loadXMLFromString(response);
        
        String decryptedTextNode = ((Element)decryptedDocument.getElementsByTagName("encrypted-part").item(0)).getTextContent();
        String decryptedAttribute = ((Element)decryptedDocument.getElementsByTagName("encrypted-part").item(0)).getAttribute("addidtional-secret");
        
        // Then
        assertEquals(secretTextNode, decryptedTextNode);
        assertEquals(secretAttribute, decryptedAttribute);
    }
}
