package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.services.BooksService;

@Controller
@RequestMapping("/api/v1/books")
public class BooksController {

	@Autowired
	BooksService booksService;

	@GetMapping("/getAllBooks")
	public ResponseEntity<List<Books>> getAllBooks() {
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/getBook/{id}")
	public ResponseEntity<Books> getBookById(@PathVariable long id) {
		return ResponseEntity.ok(booksService.getBooksDetailsById(id));
	}

	@PostMapping("/addBook")
	public ResponseEntity<Books> addBook(@Validated @RequestBody Books book) {
		return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
	}

	@PostMapping("/addBooks")
	public ResponseEntity<List<Books>> addBooks(@Validated @RequestBody List<Books> books) {
		return new ResponseEntity<List<Books>>(booksService.addBooks(books), HttpStatus.CREATED);
	}

	@PutMapping("/updateBook/{id}")
	public ResponseEntity<Books> updateBook(@PathVariable long id, @Validated @RequestBody Books books) {
		return ResponseEntity.ok(booksService.updateBooksDetails(id, books));
	}

	@PutMapping("/inActiveBook/{id}")
	public ResponseEntity<Books> deActivateBook(@PathVariable long id) {
		return ResponseEntity.ok(booksService.markBookAsInactice(id));
	}
}
