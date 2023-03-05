package com.hospitalmanagement.hospitalmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalmanagement.hospitalmanagement.services.SeleniumService;

@RestController
public class SeleniumController {
	
	@Autowired
	SeleniumService seleniumService;
	
	@GetMapping(path = "/v1/selenium")
	public String getUrlTitle(@RequestParam String url) {
		System.out.println("Request landed on v1");
		return seleniumService.getUrlTitle(url);
	}
	
	@GetMapping(path = "/v2/selenium")
	public String getUrlTitleV2(@RequestParam String url) {
		System.out.println("Request Landed on V2");
		return seleniumService.getUrlTitle(url);
	}

}
