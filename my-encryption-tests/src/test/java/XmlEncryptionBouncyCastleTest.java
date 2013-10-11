import static org.junit.Assert.*;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;


/*
 *TODO: Use Cipher.init(opmode) to change padding, mode of operation. 
 **/

public class XmlEncryptionBouncyCastleTest {
    private SecretKey aesKey;
    private SecretKey serpentKey;
    
    @Before
    public void setUp() {
        try {
            // Programatically install bouncycastle provider for serpent support.
            java.security.Security.addProvider(new BouncyCastleProvider());
            
            // Use bouncycastle provider's implementation of encryption algorithms. 
            KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES", "BC");
            aesKeyGen.init(256);
            aesKey = aesKeyGen.generateKey();
            
            KeyGenerator serpentKeyGenerator = KeyGenerator.getInstance("serpent", "BC");
            serpentKeyGenerator.init(256);
            serpentKey = serpentKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @Test
    public void canEncryptUsingAES256() {
        try {
            // Given
            Cipher cipher = Cipher.getInstance("AES");
            // When
            byte[] data = "A simple string".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encryptedBytes = cipher.doFinal(data);
            // Then
            assertNotNull(encryptedBytes);
            assertTrue(encryptedBytes.length > 0);
            System.out.println(new String(encryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
           throw new RuntimeException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @Test
    public void canEncryptUsingSerpent256() {
        try {
            // Given
            Cipher cipher = Cipher.getInstance("serpent");
            // When
            byte[] data = "A simple string".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, serpentKey);
            byte[] encryptedBytes = cipher.doFinal(data);
            // Then
            assertNotNull(encryptedBytes);
            assertTrue(encryptedBytes.length > 0);
            System.out.println(new String(encryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
           throw new RuntimeException(e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e.getMessage());
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
