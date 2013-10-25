import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import com.encryption.testing.client.HttpClient;
import com.encryption.testing.helpers.XMLEncryptionHelper;

import static org.hamcrest.Matchers.endsWith;

public class XMLEncryptionHelperTest {
    
    @BeforeClass
    public static void setUpBeforeClass() {
        // Programatically install bouncycastle provider.
        java.security.Security.addProvider(new BouncyCastleProvider());
        org.apache.xml.security.Init.init();
    }
    
    @Test
    public void canReplaceSecretElementsCorrectly() {
        // Given
        byte[] publicKey = Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChv25g1JTG8/r1/qq1x3/3tQYHRWvp8OO/r2V9kHFdxV0avJfWuEclIeqZyNB24AxKtgvTJBfoNa5hoTdwYFqwVXe0ewDmUuJksSQPHdUONuXe3JBrMyKXAQnC6hYQ7Y7VvQMaHKsWjtdTGXux7sBxa+8H0eqwwzotMPnpnWMtvQIDAQAB"); // some public key
        String xmlFile = getClass().getResource("/com/encryption/testing/messages/DummyMessage.xml").getFile();
        Document documentToEncrypt = XMLEncryptionHelper.loadXMLFromFile(xmlFile);
        // When
        Document processedDocument = XMLEncryptionHelper.prepareXmlEncryptionMessage(publicKey, documentToEncrypt);
        // Then
        assertThat(xmlFile, endsWith("DummyMessage.xml"));
        assertTrue(processedDocument.getElementsByTagName("xenc:EncryptedData").getLength() > 0);
        assertTrue(processedDocument.getElementsByTagName("xenc:EncryptionMethod").getLength() > 0);
        assertTrue(processedDocument.getElementsByTagName("xenc:EncryptedKey").getLength() > 0);
        assertTrue(processedDocument.getElementsByTagName("xenc:CipherData").getLength() > 0);
        //System.out.println(XMLEncryptionHelper.documentToXmlString(processedDocument));
    }
}
