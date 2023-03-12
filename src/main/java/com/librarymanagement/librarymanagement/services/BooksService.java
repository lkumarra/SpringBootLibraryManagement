package com.librarymanagement.librarymanagement.services;

import java.util.List;
import java.util.Map;

import com.librarymanagement.librarymanagement.entities.Books;

public interface BooksService {

	public List<Books> getAllBooks();

	public Books getBooksDetailsById(long id);

	public Books updateBooksDetails(long id, Books books);

	public Books addBook(Books book);

	public Books markBookAsInactice(long id);

	public List<Books> addBooks(List<Books> book);
	
	public Map<String, String> deleteBook(long id);

}
