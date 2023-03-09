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
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.services.BooksService;

@Controller
@RequestMapping("/books")
public class BooksController {

	@Autowired
	BooksService booksService;

	@GetMapping("/getAllBooks")
	public ResponseEntity<List<Books>> getAllBooks() {
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}

	@PostMapping("/addBook")
	public ResponseEntity<Books> addBook(@Validated @RequestBody Books book) {
		return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
	}

	@PostMapping("/addBooks")
	public ResponseEntity<List<Books>> addBooks(@Validated @RequestBody List<Books> books) {
		return new ResponseEntity<List<Books>>(booksService.addBooks(books), HttpStatus.CREATED);
	}
}
