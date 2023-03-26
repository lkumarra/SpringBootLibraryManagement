package com.librarymanagement.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.librarymanagement.librarymanagement.dtos.LoginDTO;
import com.librarymanagement.librarymanagement.services.LoginService;

@Controller
@RequestMapping("/api/v1/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	@GetMapping("/token")
	public ResponseEntity<LoginDTO> generateToke(@RequestHeader("client_id") String clientId,
			@RequestHeader("client_secret") String clientSecret) {
		return ResponseEntity.ok(loginService.login(clientId, clientSecret));
	}
}
