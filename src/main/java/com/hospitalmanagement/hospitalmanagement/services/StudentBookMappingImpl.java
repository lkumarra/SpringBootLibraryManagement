package com.hospitalmanagement.hospitalmanagement.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hospitalmanagement.hospitalmanagement.entities.Books;
import com.hospitalmanagement.hospitalmanagement.entities.StudentBookMapping;
import com.hospitalmanagement.hospitalmanagement.entities.Students;
import com.hospitalmanagement.hospitalmanagement.repositories.BooksRepository;
import com.hospitalmanagement.hospitalmanagement.repositories.StudentBookMappingRepository;
import com.hospitalmanagement.hospitalmanagement.repositories.StudentsRepository;

@Service
public class StudentBookMappingImpl implements StudentBookMappingService {

	@Autowired
	public StudentBookMappingRepository studentBookMappingRepo;

	@Autowired
	public BooksRepository booksRepo;

	@Autowired
	public StudentsRepository studentRepository;

	@Override
	public HashMap<String, Object> issueBook(StudentBookMapping studentBookMapping) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (!studentRepository.existsById(studentBookMapping.getStudentId())
				|| studentRepository.findById(studentBookMapping.getStudentId()).get().isStatus() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student id does not exists in our records.");
		} else if (!booksRepo.existsById(studentBookMapping.getBookId())
				|| booksRepo.findById(studentBookMapping.getBookId()).get().isStatus() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book id does not exists in our records.");
		} else if (Objects
				.nonNull(studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true))) {
			if (studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true).isBookIssued()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Given book id is already issued so can not issue again.");
			} else {
				long issuedDate = Instant.now().toEpochMilli();
				studentBookMapping.setIssuedDate(issuedDate);
				studentBookMapping.setBookIssued(true);
				studentBookMappingRepo.save(studentBookMapping);
				Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
				Books books = booksRepo.findById(studentBookMapping.getStudentId()).get();
				String message = String.format("Book %s is issued to %s", books.getBookName(),
						students.getStudentName());
				hashMap.put("message", message);

			}
		} else {
			long issuedDate = Instant.now().toEpochMilli();
			studentBookMapping.setIssuedDate(issuedDate);
			studentBookMapping.setBookIssued(true);
			studentBookMappingRepo.save(studentBookMapping);
			Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
			Books books = booksRepo.findById(studentBookMapping.getBookId()).get();
			String message = String.format("Book %s is issued to %s", books.getBookName(), students.getStudentName());
			hashMap.put("message", message);
		}
		return hashMap;
	}

	@Override
	public HashMap<String, Object> returnBook(StudentBookMapping studentBookMapping) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (!studentRepository.existsById(studentBookMapping.getStudentId())
				|| studentRepository.findById(studentBookMapping.getStudentId()).get().isStatus() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student id does not exists in our records.");
		} else if (!booksRepo.existsById(studentBookMapping.getBookId())
				|| booksRepo.findById(studentBookMapping.getBookId()).get().isStatus() == false) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book id does not exists in our records.");
		} else if (Objects
				.isNull(studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given book id does not issued to any student");
		} else if (!studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true)
				.isBookIssued()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Can not return the book as this book is not issued to student");
		} else {
			StudentBookMapping getIssuedBook = studentBookMappingRepo
					.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
			if (getIssuedBook.getStudentId() != studentBookMapping.getStudentId()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Book is not issued to student so can not return the book");
			} else {
				long returnDate = Instant.now().toEpochMilli();
				long issuedDate = getIssuedBook.getIssuedDate();
				studentBookMapping.setBookIssued(false);
				studentBookMapping.setId(getIssuedBook.getId());
				studentBookMapping.setReturnDate(returnDate);
				studentBookMapping.setIssuedDate(issuedDate);
				studentBookMappingRepo.save(studentBookMapping);
				Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
				Books books = booksRepo.findById(studentBookMapping.getBookId()).get();
				String message = String.format("Book %s is returned from %s", books.getBookName(),
						students.getStudentName());
				hashMap.put("message", message);
			}
		}
		return hashMap;

	}

	@Override
	public HashMap<String, Object> getAllIssuedBookToStudent(long studentId) {
		List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
				true);
		if (studentBookMappings.isEmpty()) {
			return new HashMap<String, Object>();
		} else {
			HashMap<String, Object> booksMap = new HashMap<String, Object>();
			List<Books> books = new ArrayList<Books>();
			studentBookMappings.forEach(student -> {
				Books book = booksRepo.findById(student.getBookId()).get();
				books.add(book);
			});
			Students student = studentRepository.findById(studentId).get();
			booksMap.put("studentDetails", student);
			booksMap.put("booksDetails", books);
			return booksMap;
		}
	}

	@Override
	public List<HashMap<String, Object>> getAllBooksIssuedToStudents() {
		List<HashMap<String, Object>> studentBooksList = new ArrayList<HashMap<String, Object>>();
		List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByBookIssued(true);
		studentBookMappings.forEach(studentMappping -> {
			HashMap<String, Object> jsonObject = getBooksIssued(studentMappping.getStudentId());
			if (!studentBooksList.contains(jsonObject)) {
				studentBooksList.add(jsonObject);
			}
		});
		return studentBooksList;
	}

	public HashMap<String, Object> getBooksIssued(long studentId) {
		List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
				true);
		HashMap<String, Object> studentBookHashMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> booksList = new ArrayList<HashMap<String, Object>>();
		studentBookMappings.forEach(book -> {
			Books books = booksRepo.findById(book.getBookId()).get();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
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
		studentBookHashMap.put("booksIssued", booksList);
		return studentBookHashMap;
	}

}
