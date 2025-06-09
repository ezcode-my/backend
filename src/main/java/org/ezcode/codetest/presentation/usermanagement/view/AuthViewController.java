package org.ezcode.codetest.presentation.usermanagement.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthViewController {

	@GetMapping("/ezlogin")
	public String loginPage(){
		return "login-page.html";
	}

	@GetMapping("/login/oauth")
	public String OAuthLogin(){
		return "/login";
	}
}
