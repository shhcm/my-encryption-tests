package com.encryption.testing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")

public class BaseController {
	@RequestMapping(value="welcome", method=RequestMethod.GET)
	public String welcome(ModelMap model) {
		model.addAttribute("subject", "Testing XML Encryption");
		//Spring uses InternalResourceViewResolver and returns back index.jsp
		return "index";
	}
}
