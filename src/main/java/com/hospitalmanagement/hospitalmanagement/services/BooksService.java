package com.hospitalmanagement.hospitalmanagement.services;

import java.util.List;

import com.hospitalmanagement.hospitalmanagement.entities.Books;

public interface BooksService {

	public List<Books> getAllBooks();

	public Books getBooksDetailsById(long id);

	public Books updateBooksDetails(long id, Books books);

	public Books addBook(Books book);

	public Books markBookAsInactice(long id);

	public List<Books> addBooks(List<Books> book);

}
