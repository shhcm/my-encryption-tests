package com.encryption.testing.helpers;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.keys.KeyInfo;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


@Component
public class XMLEncryptionHelper {
    
    public static Document loadXMLFromString(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xml)));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static Document loadXMLFromFile(String file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new FileInputStream(file)));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static String documentToXmlString(Document document) {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            throw new RuntimeException(e.getMessage());
        }
        return writer.toString();
    }
    
    public static Document prepareXmlEncryptionMessage(byte[] publicKey, Document message ) {
        
        
        try {
            // Create a key object for the rsa key encryption key. Key information (algorithm, encoding format) should be retrieved from server as well, instead of hardcodig this here.
            X509EncodedKeySpec keySpecKeyEncryption = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            PublicKey keyEncryptionKey = keyFactory.generatePublic(keySpecKeyEncryption);
            // Prepare the <EncryptionData> Element`s encrypted key element.
            SecretKey symmetricKey = generateSymmetricKey();
            KeyInfo keyInfoElement = prepareKeyInfo(message, keyEncryptionKey, symmetricKey);
            
            NodeList nodesToBeEncrypted = message.getElementsByTagName("encrypted-part");
            if(nodesToBeEncrypted.getLength() > 1) {
                throw new RuntimeException("encrypted-part is not unique!");
            }
            else if (nodesToBeEncrypted.getLength() == 0) {
                return null;
            }
            // Now encrypt the part of the document.
            XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_256);
            xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);
            EncryptedData encyptedDataElement = xmlCipher.getEncryptedData(); // Configure the encryptedData element.
            encyptedDataElement.setKeyInfo(keyInfoElement);
            
            return xmlCipher.doFinal(message, (Element)nodesToBeEncrypted.item(0));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (XMLEncryptionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyInfo prepareKeyInfo(  Document message,
            PublicKey keyEncryptionKey,
            SecretKey symmetricKey)
    throws XMLEncryptionException {
        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_OAEP);
        keyCipher.init(XMLCipher.WRAP_MODE, keyEncryptionKey);
        EncryptedKey encryptedKey = keyCipher.encryptKey(message, symmetricKey);
        KeyInfo keyInfoElement = new KeyInfo(message);
        keyInfoElement.add(encryptedKey);
        return keyInfoElement;
    }
    
    private static SecretKey generateSymmetricKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGenerator =
        KeyGenerator.getInstance("AES", "BC");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }
}
