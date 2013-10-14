import static org.junit.Assert.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class StrongCryptoAllowedTest {
    private SecretKey aesKey;
    private SecretKey serpentKey;
    private byte[] iv;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        // Programatically install bouncycastle provider for serpent support.
        java.security.Security.addProvider(new BouncyCastleProvider());
    }
    
    @Before
    public void setUp() {
        // Set up 16 byte initialisation vector.
        iv = "0123456789123456".getBytes();
    }
    
    @Test
    public void canEncryptUsingAES256() {
        try {
            // Given
            KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES", "BC");
            aesKeyGen.init(256);
            aesKey = aesKeyGen.generateKey();
            IvParameterSpec ips = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            // When
            byte[] data = "A simple string".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ips);
            byte[] encryptedBytes = cipher.doFinal(data);
            // Then
            assertTrue(encryptedBytes.length == 16);
            assertTrue(256 <= Cipher.getMaxAllowedKeyLength("AES/CBC/PKCS7Padding"));
            cipher.init(Cipher.DECRYPT_MODE, aesKey, ips);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            assertEquals(new String(data), new String(decryptedBytes));
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
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @Test
    public void canEncryptUsingSerpent256() {
        try {
            // Given
            KeyGenerator serpentKeyGenerator = KeyGenerator.getInstance("serpent", "BC");
            serpentKeyGenerator.init(256);
            serpentKey = serpentKeyGenerator.generateKey();
            IvParameterSpec ips = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("serpent/CBC/PKCS7Padding");
            // When
            byte[] data = "A simple string".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, serpentKey, ips);
            byte[] encryptedBytes = cipher.doFinal(data);
            // Then
            assertEquals(16, encryptedBytes.length);
            assertTrue(256 <= Cipher.getMaxAllowedKeyLength("serpent/CBC/PKCS7Padding"));
            cipher.init(Cipher.DECRYPT_MODE, serpentKey, ips);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            assertEquals(new String(data), new String(decryptedBytes));
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
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @Test
    public void canEncryptUsingRsa2048() {
        try {
            // Given
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("rsa", "BC");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            
            // When
            byte[] data = "A simple string".getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedBytes = cipher.doFinal(data);
            
            // Then
            assertTrue(encryptedBytes.length > 0);
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            assertEquals(new String(data), new String(decryptedBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
