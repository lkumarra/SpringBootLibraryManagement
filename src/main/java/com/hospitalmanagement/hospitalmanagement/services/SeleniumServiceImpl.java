package com.hospitalmanagement.hospitalmanagement.services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class SeleniumServiceImpl implements SeleniumService
{

	
	@Override
	public String getUrlTitle(String url) {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		String title = driver.getTitle();
		driver.quit();
		return title;
	}

}
