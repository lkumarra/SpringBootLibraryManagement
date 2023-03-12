package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.librarymanagement.librarymanagement.constants.StudentBookMappingErrorMessages;
import com.librarymanagement.librarymanagement.entities.Books;
import com.librarymanagement.librarymanagement.entities.StudentBookMapping;
import com.librarymanagement.librarymanagement.entities.Students;
import com.librarymanagement.librarymanagement.repositories.BooksRepository;
import com.librarymanagement.librarymanagement.repositories.StudentBookMappingRepository;
import com.librarymanagement.librarymanagement.repositories.StudentsRepository;
import com.librarymanagement.librarymanagement.services.StudentBookMappingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentBookMappingImpl implements StudentBookMappingService {

	private final String bookIssuedMessage = "Book '%s' with bookId '%s' is issued to student '%s' with roll no '%s'";
	private final String bookReturnedMessage = "Book '%s' with bookId is returned from student '%s' with roll no '%s'";

	@Autowired
	public StudentBookMappingRepository studentBookMappingRepo;

	@Autowired
	public BooksRepository booksRepo;

	@Autowired
	public StudentsRepository studentRepository;

	@Autowired
	public ResponseStatusUtility responseStatusUtility;

	private boolean isStudentExistsAndActive(long studentId) {
		return (studentRepository.existsById(studentId)
				&& studentRepository.findById(studentId).get().isStatus() == true) ? true : false;
	}

	private boolean isBookExistsAndActive(long bookId) {
		return (booksRepo.existsById(bookId) && booksRepo.findById(bookId).get().isStatus() == true) ? true : false;
	}

	private String issueBookToStudent(StudentBookMapping studentBookMapping) {
		long issuedDate = Instant.now().toEpochMilli();
		studentBookMapping.setIssuedDate(issuedDate);
		studentBookMapping.setBookIssued(true);
		studentBookMappingRepo.save(studentBookMapping);
		Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
		Books books = booksRepo.findById(studentBookMapping.getStudentId()).get();
		String message = String.format(bookIssuedMessage, books.getBookName(), books.getBookId(),
				students.getStudentName(), students.getRollNo());
		return message;
	}

	private String returnBookFromStudent(StudentBookMapping studentBookMapping) {
		StudentBookMapping getIssuedBook = studentBookMappingRepo
				.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
		long returnDate = Instant.now().toEpochMilli();
		long issuedDate = getIssuedBook.getIssuedDate();
		studentBookMapping.setBookIssued(false);
		studentBookMapping.setId(getIssuedBook.getId());
		studentBookMapping.setReturnDate(returnDate);
		studentBookMapping.setIssuedDate(issuedDate);
		studentBookMappingRepo.save(studentBookMapping);
		Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
		Books books = booksRepo.findById(studentBookMapping.getBookId()).get();
		String message = String.format(bookReturnedMessage, books.getBookName(), books.getBookId(),
				students.getStudentName(), students.getRollNo());
		return message;
	}

	@Override
	public HashMap<String, Object> issueBook(StudentBookMapping studentBookMapping) {
		HashMap<String, Object> hashMap = Maps.newHashMap();
		if (!isStudentExistsAndActive(studentBookMapping.getStudentId()))
			throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.STUDENT_NOT_EXISTS);
		else if (!isBookExistsAndActive(studentBookMapping.getBookId()))
			throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_EXISTS);
		else if (Objects
				.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true))) {
			if (studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true).isBookIssued())
				throw responseStatusUtility
						.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_ALREADY_ISSUED);
			else {
				try {
					hashMap.put("message", issueBookToStudent(studentBookMapping));
				} catch (Exception e) {
					log.error("Error occured while issue the book to student with error message : {}", e.getMessage());
					throw responseStatusUtility
							.internalServerErrorStatusException("Error occured while issue the book to student");
				}
			}
		} else {
			try {
				hashMap.put("message", issueBookToStudent(studentBookMapping));
			} catch (Exception e) {
				log.error("Error occured while issue the book to student with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while issue the book to student");
			}
		}
		return hashMap;
	}

	@Override
	public HashMap<String, Object> returnBook(StudentBookMapping studentBookMapping) {
		HashMap<String, Object> hashMap = Maps.newHashMap();
		if (!isStudentExistsAndActive(studentBookMapping.getStudentId()))
			throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.STUDENT_NOT_EXISTS);
		else if (!isBookExistsAndActive(studentBookMapping.getBookId()))
			throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_EXISTS);
		else if (Objects.isNull(studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true)))
			throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_ISSUED);
		else if (!studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true).isBookIssued())
			throw responseStatusUtility
					.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_ISSUED_TO_ANY_STUDENT);
		else {
			StudentBookMapping getIssuedBook = studentBookMappingRepo
					.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
			if (getIssuedBook.getStudentId() != studentBookMapping.getStudentId())
				throw responseStatusUtility
						.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_ISSUED_TO_STUDENT);
			else {
				try {
					hashMap.put("message", returnBookFromStudent(studentBookMapping));
				} catch (Exception e) {
					log.error("Error occured while returing the book from student with error message : {}",
							e.getMessage());
					throw responseStatusUtility
							.internalServerErrorStatusException("Error occured while returing the book");
				}
			}
		}
		return hashMap;

	}

	@Override
	public HashMap<String, Object> getAllIssuedBookToStudent(long studentId) {
		List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
				true);
		if (studentBookMappings.isEmpty())
			return new HashMap<String, Object>();
		else {
			try {
				HashMap<String, Object> booksMap = Maps.newHashMap();
				List<Books> books = Lists.newArrayList();
				studentBookMappings.forEach(student -> {
					Books book = booksRepo.findById(student.getBookId()).get();
					books.add(book);
				});
				Students student = studentRepository.findById(studentId).get();
				booksMap.put("studentDetails", student);
				booksMap.put("booksDetails", books);
				return booksMap;
			} catch (Exception e) {
				log.error("Error occured while fetching the issued books to a student with error messge : {}",
						e.getMessage());
				throw responseStatusUtility.internalServerErrorStatusException(
						"Error occured while fetching the issued books to a student");
			}
		}
	}

	@Override
	public List<HashMap<String, Object>> getAllBooksIssuedToStudents() {
		try {
			List<HashMap<String, Object>> studentBooksList = Lists.newArrayList();
			List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByBookIssued(true);
			studentBookMappings.forEach(studentMappping -> {
				HashMap<String, Object> jsonObject = getBooksIssued(studentMappping.getStudentId());
				if (!studentBooksList.contains(jsonObject)) {
					studentBooksList.add(jsonObject);
				}
			});
			return studentBooksList;
		} catch (Exception e) {
			log.error("Error occured while fetching all issued books with error message : {}", e.getMessage());
			throw responseStatusUtility
					.internalServerErrorStatusException("Error occured while fetching all issued books");
		}
	}

	public HashMap<String, Object> getBooksIssued(long studentId) {
		List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
				true);
		HashMap<String, Object> studentBookHashMap = Maps.newHashMap();
		List<HashMap<String, Object>> booksList = Lists.newArrayList();
		studentBookMappings.forEach(book -> {
			Books books = booksRepo.findById(book.getBookId()).get();
			HashMap<String, Object> hashMap = Maps.newHashMap();
			hashMap.put("bookName", books.getBookName());
			hashMap.put("department", books.getDepartment());
			hashMap.put("bookAuthor", books.getBookAuthor());
			hashMap.put("bookId", books.getBookId());
			hashMap.put("id", books.getId());
			booksList.add(hashMap);
		});
		Students student = studentRepository.findById(studentId).get();
		studentBookHashMap.put("studentName", student.getStudentName());
		studentBookHashMap.put("department", student.getDepartment());
		studentBookHashMap.put("rollNo", student.getRollNo());
		studentBookHashMap.put("id", student.getId());
		studentBookHashMap.put("issuedBooks", booksList);
		return studentBookHashMap;
	}

}
