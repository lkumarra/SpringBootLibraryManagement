package com.hospitalmanagement.hospitalmanagement.services;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.repositories.BooksRepository;

@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	public BooksRepository booksRepository;

	@Override
	public List<Books> getAllBooks() {
		return (List<Books>) booksRepository.findAll();
	}

	@Override
	public Books getBooksDetailsById(long id) {
		if (!booksRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No record exists for given id");
		} else {
			return booksRepository.findById(id).get();
		}
	}

	@Override
	public Books updateBooksDetails(long id, Books book) {
		if (!booksRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No record exists for given id");
		} else {
			book.setId(id);
			return booksRepository.save(book);
		}
	}

	@Override
	public Books addBook(Books book) {
		if (Objects.isNull(book.getBookName()) || book.getBookName().isEmpty() || book.getBookName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book name can not be null or empty");
		} else if (Objects.isNull(book.getBookAuthor()) || book.getBookAuthor().isEmpty()
				|| book.getBookAuthor().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book author can not be null or empty");
		} else if (Objects.isNull(book.getDepartment()) || book.getDepartment().isEmpty()
				|| book.getDepartment().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department can not be null or empty");
		} else if (!booksRepository.findBooksByBookId(book.getBookId()).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Book Id should be unique given book id is already exists");
		} else {
			long time = Instant.now().toEpochMilli();
			book.setCreatedAt(time);
			book.setUpdatedAt(time);
			book.setStatus(true);
			return booksRepository.save(book);
		}
	}

	@Override
	public Books markBookAsInactice(long id) {
		if (!booksRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No record exists for given id");
		} else {
			Books book = booksRepository.findById(id).get();
			book.setStatus(false);
			return booksRepository.save(book);
		}
	}

}
