package com.librarymanagement.librarymanagement.controller;

import java.util.List;
import java.util.Map;
//import javax.annotation.security.RolesAllowed;

import com.librarymanagement.librarymanagement.modals.FilterBooksModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.services.BooksService;

@Controller
@RequestMapping("/api/v1/books")
public class BooksController {

	@Autowired
	BooksService booksService;

	@GetMapping("/getAllBooks")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> getAllBooks() {
		return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/getBook/{id}")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> getBookById(@PathVariable long id) {
		return ResponseEntity.ok(booksService.getBooksDetailsById(id));
	}

	@PostMapping("/addBook")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> addBook(@Validated @RequestBody Books book) {
		return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
	}

	@PostMapping("/addBooks")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> addBooks(@Validated @RequestBody List<Books> books) {
		return new ResponseEntity<List<Books>>(booksService.addBooks(books), HttpStatus.CREATED);
	}

	@PutMapping("/updateBook/{id}")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> updateBook(@PathVariable long id, @Validated @RequestBody Books books) {
		return ResponseEntity.ok(booksService.updateBooksDetails(id, books));
	}

	@PutMapping("/inActiveBook/{id}")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Books> deActivateBook(@PathVariable long id) {
		return ResponseEntity.ok(booksService.markBookAsInactive(id));
	}

	@DeleteMapping("/deleteBook/{id}")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Map<String, String>> deleteBook(@PathVariable long id) {
		return ResponseEntity.ok(booksService.deleteBook(id));
	}

	@GetMapping("/searchBook")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> searchStudents(@RequestParam String bookName){
		return ResponseEntity.ok(booksService.searchBooks(bookName));
	}

	@PostMapping("/filterBooks")
	//@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Books>> filterBooks(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize,@Validated @RequestBody FilterBooksModal filterBooksModal){
		return ResponseEntity.ok(booksService.filterBooks(pageNo, pageSize,filterBooksModal));
	}

}
