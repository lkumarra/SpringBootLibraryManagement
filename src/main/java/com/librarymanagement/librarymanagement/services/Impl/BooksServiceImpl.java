package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.librarymanagement.librarymanagement.constants.BooksErrorMessages;
import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.repositories.BooksRepository;
import com.librarymanagement.librarymanagement.repositories.StudentBookMappingRepository;
import com.librarymanagement.librarymanagement.services.BooksService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BooksServiceImpl implements BooksService {

	@Autowired
	public BooksRepository booksRepository;

	@Autowired
	public ResponseStatusUtility responseStatusUtility;

	@Autowired
	public StudentBookMappingRepository studentBookMappingRepo;

	@Override
	public List<Books> getAllBooks() {
		try {
			return (List<Books>) booksRepository.findAll();
		} catch (Exception e) {
			log.error("Error occured while fetching the books with error message : {}", e.getMessage());
			throw responseStatusUtility.internalServerErrorStatusException("Error occured while fetching the books");
		}
	}

	@Override
	public Books getBooksDetailsById(long id) {
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
		} else {
			try {
				return booksRepository.findById(id).get();
			} catch (Exception e) {
				log.error("Error occured while fetching the book with error message : {}", e.getMessage());
				throw responseStatusUtility.internalServerErrorStatusException("Error occured while fetching the book");
			}
		}
	}

	@Override
	public Books updateBooksDetails(long id, Books book) {
		if (!booksRepository.existsById(id))
			throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
		else {
			if (isBookNameInvalid(book))
				throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_NAME);
			else if (isBookAuthorInvalid(book))
				throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_AUTHOR);
			else if (isBookDepartmentInvalid(book))
				throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_DEPARTMENT);
			else {
				try {
					book.setId(id);
					Books books = booksRepository.findById(id).get();
					book.setCreatedAt(books.getCreatedAt());
					long updatedAt = Instant.now().toEpochMilli();
					book.setUpdatedAt(updatedAt);
					return booksRepository.save(book);
				} catch (Exception e) {
					log.error("Error occured while saving the book with error message:{}", e.getMessage());
					throw responseStatusUtility
							.internalServerErrorStatusException("Error occured while updating the book");
				}
			}
		}
	}

	@Override
	public Books markBookAsInactice(long id) {
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
		} else if (Objects.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(id, true)))
			throw responseStatusUtility.conflictStatusException(BooksErrorMessages.CAN_NOT_INACTIVE_BOOK);
		else {
			try {
				Books book = booksRepository.findById(id).get();
				long updatedAt = Instant.now().toEpochMilli();
				book.setUpdatedAt(updatedAt);
				book.setStatus(false);
				return booksRepository.save(book);
			} catch (Exception e) {
				log.error("Error occured while marking the book inactive with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while marking the student as inactive");
			}
		}
	}

	@Override
	public Books addBook(Books book) {
		if (isBookNameInvalid(book))
			throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_NAME);
		else if (isBookAuthorInvalid(book))
			throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_AUTHOR);
		else if (isBookDepartmentInvalid(book))
			throw responseStatusUtility.badRequestStatusException(BooksErrorMessages.INVALID_BOOK_DEPARTMENT);
		else if (!booksRepository.findBooksByBookId(book.getBookId()).isEmpty())
			throw responseStatusUtility.conflictStatusException(BooksErrorMessages.BOOK_ID_DUPLICATE);
		else {
			try {
				long time = Instant.now().toEpochMilli();
				book.setCreatedAt(time);
				book.setUpdatedAt(time);
				book.setStatus(true);
				return booksRepository.save(book);
			} catch (Exception e) {
				log.error("Error occured while adding the book with error message : {}", e.getMessage());
				throw responseStatusUtility.internalServerErrorStatusException("Error occured while adding the book");
			}
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
				throw responseStatusUtility.badRequestStatusException(
						String.format(BooksErrorMessages.INVALID_BOOK_DATA, book.toString()));
		}
		for (Books book : booksList) {
			if (booksId.contains(book.getBookId()))
				throw responseStatusUtility.conflictStatusException(
						String.format(BooksErrorMessages.DUPLICATE_BOOK_IDS, book.getBookId()));
			else
				booksId.add(book.getBookId());
		}
		for (Books book : booksList) {
			try {
				booksListResponse.add(addBook(book));
			} catch (Exception e) {
				log.error("Error occured while adding the books with error message : {}", e.getMessage());
				throw responseStatusUtility.internalServerErrorStatusException("Error occured while adding the book");
			}
		}
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

	@Override
	public Map<String, String> deleteBook(long id) {
		Map<String, String> hashMap = Maps.newConcurrentMap();
		if (!booksRepository.existsById(id)) {
			throw responseStatusUtility.notFoundStatusException(BooksErrorMessages.BOOK_NOT_EXISTS);
		} else if (Objects.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(id, true)))
			throw responseStatusUtility.conflictStatusException(BooksErrorMessages.CAN_NOT_DELETE_BOOK);
		else {
			try {
				booksRepository.deleteById(id);
				hashMap.put("message", "Success");
				return hashMap;
			} catch (Exception e) {
				log.error("Error occured while deleting the book with error message : {}", e.getMessage());
				throw responseStatusUtility.internalServerErrorStatusException("Error occured while deleting the book");
			}
		}
	}

}
