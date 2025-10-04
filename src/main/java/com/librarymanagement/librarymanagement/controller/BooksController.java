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
@RequestMapping("/api/v1")
public class BooksController {

    @Autowired
    BooksService booksService;

    @GetMapping("/books")
    public ResponseEntity<List<Books>> getAllBooks() {
        return new ResponseEntity<List<Books>>(booksService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable long id) {
        return ResponseEntity.ok(booksService.getBooksDetailsById(id));
    }

    @PostMapping("/book")
    public ResponseEntity<Books> addBook(@Validated @RequestBody Books book) {
        return new ResponseEntity<Books>(booksService.addBook(book), HttpStatus.CREATED);
    }

    @PostMapping("/books")
    public ResponseEntity<List<Books>> addBooks(@Validated @RequestBody List<Books> books) {
        return new ResponseEntity<List<Books>>(booksService.addBooks(books), HttpStatus.CREATED);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable long id, @Validated @RequestBody Books books) {
        return ResponseEntity.ok(booksService.updateBooksDetails(id, books));
    }

    @PutMapping("/inactive/book/{id}")
    public ResponseEntity<Books> deActivateBook(@PathVariable long id) {
        return ResponseEntity.ok(booksService.markBookAsInactive(id));
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Map<String, String>> deleteBook(@PathVariable long id) {
        return ResponseEntity.ok(booksService.deleteBook(id));
    }

    @GetMapping("/search/book")
    public ResponseEntity<List<Books>> searchStudents(@RequestParam String bookName) {
        return ResponseEntity.ok(booksService.searchBooks(bookName));
    }

    @PostMapping("/filter/book")
    public ResponseEntity<List<Books>> filterBooks(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int pageSize, @Validated @RequestBody FilterBooksModal filterBooksModal) {
        return ResponseEntity.ok(booksService.filterBooks(pageNo, pageSize, filterBooksModal));
    }

}
