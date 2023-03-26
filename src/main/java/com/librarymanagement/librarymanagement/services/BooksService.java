package com.librarymanagement.librarymanagement.services;

import java.util.List;
import java.util.Map;
import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.modals.FilterBooksModal;

public interface BooksService {

	List<Books> getAllBooks();

	Books getBooksDetailsById(long id);

	Books updateBooksDetails(long id, Books books);

	Books addBook(Books book);

	Books markBookAsInactive(long id);

	List<Books> addBooks(List<Books> book);
	
	Map<String, String> deleteBook(long id);

	List<Books> searchBooks(String bookName);

	List<Books> filterBooks(int pageNo, int pageSize,FilterBooksModal filterBooksModal);

}
