package com.encryption.testing.controller;

import java.io.IOException;
import java.io.Writer;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

import com.encryption.testing.helpers.XMLEncryptionHelper;


@Service
@Controller
@RequestMapping("/")
public class BaseController {
    
    private PublicKeyRepository publicKeyRepository;
    
    @Autowired
    public void setPublicKeyRepository(PublicKeyRepository publicKeyRepository) {
        // The public key repository needs to have access to BouncyCastle crypto implementation. 
        java.security.Security.addProvider(new BouncyCastleProvider());
        this.publicKeyRepository = publicKeyRepository;
    }
    
    @RequestMapping(value="welcome", method=RequestMethod.GET)
    public String welcome(ModelMap model) {
        model.addAttribute("subject", "Testing XML Encryption");
        //Spring uses InternalResourceViewResolver and returns back index.jsp
        return "index";
    }

    @RequestMapping(value="getEncryptionDetails", method=RequestMethod.POST)
    public String getsetEncryptionDetails(
            @RequestParam(value="mode") String mode,
            ModelMap model) {
        model.addAttribute("mode", mode);
        return "encryptionMode";
    }
    
    @RequestMapping(value="getExample", method=RequestMethod.POST)
    public void getExample(
            @RequestBody String requestBody, Writer writer) {
        try {
            // TODO: lookup this method signature, why writer contains the http response.
            writer.write(requestBody);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * Receive an xml message that is encrypted with one of the key pairs
     *  that are stored in the PublicKeyRepository.
     * 
     * Read the security parameter from the message and decrypt the message
     * with the private key that is stored in the PublicKeyRepository for
     * this securityParameter.
     * 
     * Send back the decrypted part of the message to prove that the
     * encryption works.
     */
    @RequestMapping(value="processEncrypted", method=RequestMethod.POST)
    public void processEncrypted(
            @RequestBody String requestBody, Writer writer) {
        // we expect the xml to be contained in the request Body.
        Document encryptedDoc = XMLEncryptionHelper.loadXMLFromString(requestBody);
        //XMLEncryptionHelper.decryptXML(encryptedDoc, KeyPair asymetricKey);
    }
    
    @RequestMapping(value="getPublicKey", method=RequestMethod.GET)
    public void getPublicKey(
            @RequestParam(value="securityParameter") int securityParameter,
            Writer writer) {
        try {
            // The primary key encoding format must be known by the HttpClient in order to reinstantiate a public key from its encoding.
            // TODO: sent this information together with the algorithm name to the client.
            System.out.println(publicKeyRepository.getPublicKey(securityParameter).getFormat());
            byte[] encodedBytes = Base64.encode(publicKeyRepository.getPublicKey(securityParameter).getEncoded());
            writer.write(new String(encodedBytes));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
