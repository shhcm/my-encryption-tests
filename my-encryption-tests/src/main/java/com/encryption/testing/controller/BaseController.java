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
    
    @RequestMapping(value="getEncryptedExample", method=RequestMethod.POST)
    public void getEncryptionExample(
            @RequestBody String requestBody, Writer writer) {
        try {
            // TODO: lookup this method signature, why writer contains the http response.
            writer.write(requestBody);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
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
