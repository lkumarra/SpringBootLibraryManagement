package com.librarymanagement.librarymanagement.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.services.BooksService;

@Controller
@RequestMapping("/api/v1/books")
public class BooksController {

	@Autowired
	BooksService booksService;

	@GetMapping("/getAllBooks")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> getAllBooks() {
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/getBook/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> getBookById(@PathVariable long id) {
		return ResponseEntity.ok(booksService.getBooksDetailsById(id));
	}

	@PostMapping("/addBook")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> addBook(@Validated @RequestBody Books book) {
		return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
	}

	@PostMapping("/addBooks")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> addBooks(@Validated @RequestBody List<Books> books) {
		return new ResponseEntity<List<Books>>(booksService.addBooks(books), HttpStatus.CREATED);
	}

	@PutMapping("/updateBook/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> updateBook(@PathVariable long id, @Validated @RequestBody Books books) {
		return ResponseEntity.ok(booksService.updateBooksDetails(id, books));
	}

	@PutMapping("/inActiveBook/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> deActivateBook(@PathVariable long id) {
		return ResponseEntity.ok(booksService.markBookAsInactice(id));
	}

	@DeleteMapping("/deleteBook/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Map<String, String>> deleteBook(@PathVariable long id) {
		return ResponseEntity.ok(booksService.deleteBook(id));
	}
}
