package com.hospitalmanagement.hospitalmanagement.services.Impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.hospitalmanagement.hospitalmanagement.constants.BooksErrorMessages;
import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.repositories.BooksRepository;
import com.hospitalmanagement.hospitalmanagement.services.BooksService;

@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	public BooksRepository booksRepository;

	@Autowired
	public ResponseStatusUtility responseStatusUtility;

	@Override
	public List<Books> getAllBooks() {
		return (List<Books>) booksRepository.findAll();
	}

	@Override
	public Books getBooksDetailsById(long id) {
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.responseStatusException(HttpStatus.NOT_FOUND,
					BooksErrorMessages.BOOK_NOT_EXISTS);
		} else {
			return booksRepository.findById(id).get();
		}
	}

	@Override
	public Books updateBooksDetails(long id, Books book) {
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.responseStatusException(HttpStatus.NOT_FOUND,
					BooksErrorMessages.BOOK_NOT_EXISTS);
		} else {
			book.setId(id);
			return booksRepository.save(book);
		}
	}

	@Override
	public Books markBookAsInactice(long id) {
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.responseStatusException(HttpStatus.NOT_FOUND,
					BooksErrorMessages.BOOK_NOT_EXISTS);
		} else {
			Books book = booksRepository.findById(id).get();
			book.setStatus(false);
			return booksRepository.save(book);
		}
	}

	@Override
	public Books addBook(Books book) {
		if (isBookNameInvalid(book))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					BooksErrorMessages.INVALID_BOOK_NAME);
		else if (isBookAuthorInvalid(book))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					BooksErrorMessages.INVALID_BOOK_AUTHOR);
		else if (isBookDepartmentInvalid(book))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					BooksErrorMessages.INVALID_BOOK_DEPARTMENT);
		else if (!booksRepository.findBooksByBookId(book.getBookId()).isEmpty())
			throw responseStatusUtility.responseStatusException(HttpStatus.CONFLICT,
					BooksErrorMessages.BOOK_ID_DUPLICATE);
		else {
			long time = Instant.now().toEpochMilli();
			book.setCreatedAt(time);
			book.setUpdatedAt(time);
			book.setStatus(true);
			return booksRepository.save(book);
		}
	}

	@Override
	public List<Books> addBooks(List<Books> books) {
		List<Books> booksList = Lists.newArrayList();
		List<Books> booksListResponse = Lists.newArrayList();
		List<String> booksId = Lists.newArrayList();
		for (Books book : books) {
			if (isBookDataValid(book))
				booksList.add(book);
			else
				throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
						String.format(BooksErrorMessages.INVALID_BOOK_DATA, book.toString()));
		}
		for (Books book : booksList) {
			if (booksId.contains(book.getBookId()))
				throw responseStatusUtility.responseStatusException(HttpStatus.CONFLICT,
						String.format(BooksErrorMessages.DUPLICATE_BOOK_IDS, book.getBookId()));
			else
				booksId.add(book.getBookId());
		}
		for (Books book : booksList)
			booksListResponse.add(addBook(book));
		return booksListResponse;
	}

	private boolean isBookDataValid(Books book) {
		if (isBookNameInvalid(book))
			return false;
		else if (isBookAuthorInvalid(book))
			return false;
		else if (isBookDepartmentInvalid(book))
			return false;
		else if (!booksRepository.findBooksByBookId(book.getBookId()).isEmpty())
			return false;
		else
			return true;
	}

	private boolean isBookNameInvalid(Books book) {
		if (Objects.isNull(book.getBookName()))
			return true;
		else if (book.getBookName().isEmpty())
			return true;
		else if (book.getBookName().isBlank())
			return true;
		else
			return false;
	}

	private boolean isBookAuthorInvalid(Books book) {
		if (Objects.isNull(book.getBookAuthor()))
			return true;
		else if (book.getBookAuthor().isEmpty())
			return true;
		else if (book.getBookAuthor().isBlank())
			return true;
		else
			return false;
	}

	private boolean isBookDepartmentInvalid(Books book) {
		if (Objects.isNull(book.getDepartment()))
			return true;
		else if (book.getDepartment().isEmpty())
			return true;
		else if (book.getDepartment().isBlank())
			return true;
		else
			return false;
	}

}
