package com.encryption.testing.controller;

import java.io.IOException;
import java.io.Writer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.encryption.XMLCipher;
import org.bouncycastle.*;

@Controller
@RequestMapping("/")

public class BaseController {
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
}
