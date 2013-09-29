package com.encryption.testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
}
