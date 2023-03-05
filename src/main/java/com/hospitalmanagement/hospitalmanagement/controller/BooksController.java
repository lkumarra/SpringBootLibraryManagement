package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.services.BooksService;

@Controller
public class BooksController {
	
	@Autowired
	BooksService booksService;

	@GetMapping("/books")
	public ResponseEntity<List<Books>> getAllBooks(){
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}
	
	@PostMapping("/books")
	public ResponseEntity<Books> addBooks(@Validated @RequestBody Books book){
		return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
	}
}
