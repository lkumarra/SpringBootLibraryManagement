package com.librarymanagement.librarymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@SpringBootApplication
@EnableSwagger2
public class LibraryManagementApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}
	
	public static void startServer() {
		SpringApplication.run(LibraryManagementApplication.class);
	}
	
}
