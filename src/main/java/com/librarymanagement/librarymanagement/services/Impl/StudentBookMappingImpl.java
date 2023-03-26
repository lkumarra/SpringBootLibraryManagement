package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.*;

import com.google.common.collect.Sets;
import com.librarymanagement.librarymanagement.dtos.IssuedBookModalDTO.*;
import com.librarymanagement.librarymanagement.dtos.IssuedBookModalDTO;
import com.librarymanagement.librarymanagement.utils.ResponseStatusUtility;
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

    @Autowired
    public StudentBookMappingRepository studentBookMappingRepo;

    @Autowired
    public BooksRepository booksRepo;

    @Autowired
    public StudentsRepository studentRepository;

    @Autowired
    public ResponseStatusUtility responseStatusUtility;

    private boolean isStudentExistsAndActive(long studentId) {
        return studentRepository.existsById(studentId)
                && studentRepository.findById(studentId).get().isStatus();
    }

    private boolean isBookExistsAndActive(long bookId) {
        return booksRepo.existsById(bookId) && booksRepo.findById(bookId).get().isStatus();
    }

    private String issueBookToStudent(StudentBookMapping studentBookMapping) {
        long issuedDate = Instant.now().toEpochMilli();
        studentBookMapping.setIssuedDate(issuedDate);
        studentBookMapping.setBookIssued(true);
        studentBookMappingRepo.save(studentBookMapping);
        Students students = studentRepository.findById(studentBookMapping.getStudentId()).get();
        Books books = booksRepo.findById(studentBookMapping.getStudentId()).get();
        String bookIssuedMessage = "Book '%s' with bookId '%s' is issued to student '%s' with roll no '%s'";
        return String.format(bookIssuedMessage, books.getBookName(), books.getBookId(),
                students.getStudentName(), students.getRollNo());
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
        String bookReturnedMessage = "Book '%s' with bookId is returned from student '%s' with roll no '%s'";
        return String.format(bookReturnedMessage, books.getBookName(), books.getBookId(),
                students.getStudentName(), students.getRollNo());
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
                    log.error("Error occurred while issue the book to student with error message : {}", e.getMessage());
                    throw responseStatusUtility
                            .internalServerErrorStatusException("Error occurred while issue the book to student");
                }
            }
        } else {
            try {
                hashMap.put("message", issueBookToStudent(studentBookMapping));
            } catch (Exception e) {
                log.error("Error occurred while issue the book to student with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while issue the book to student");
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
                    log.error("Error occurred while returning the book from student with error message : {}",
                            e.getMessage());
                    throw responseStatusUtility
                            .internalServerErrorStatusException("Error occurred while returning the book");
                }
            }
        }
        return hashMap;
    }

    @Override
    public IssuedBookModalDTO getAllIssuedBookToStudent(long studentId) {
        List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
                true);
        if (studentBookMappings.isEmpty())
            throw responseStatusUtility.notFoundStatusException("No issued book found for studentId " + studentId);
        else {
            try {
                List<BooksIssued> booksIssuedList = Lists.newArrayList();
                studentBookMappings.forEach(book -> {
                    Books books = booksRepo.findById(book.getBookId()).get();
                    BooksIssued booksIssued = BooksIssued.builder()
                            .bookName(books.getBookName())
                            .bookId(books.getId())
                            .bookUniqueId(books.getBookId())
                            .bookDepartment(books.getDepartment())
                            .bookAuthor(books.getBookAuthor()).build();
                    booksIssuedList.add(booksIssued);
                });
                Students students = studentRepository.findById(studentId).get();
                return IssuedBookModalDTO.builder()
                        .studentId(students.getId())
                        .studentName(students.getStudentName())
                        .studentRollNo(students.getRollNo())
                        .studentDepartment(students.getDepartment())
                        .booksIssued(booksIssuedList).build();
            } catch (Exception e) {
                log.error("Error occurred while fetching the issued books to a student with error message : {}",
                        e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException(
                        "Error occurred while fetching the issued books to a student");
            }
        }
    }

    @Override
    public List<IssuedBookModalDTO> getAllBooksIssuedToStudents() {
        try {
            List<IssuedBookModalDTO> issuedBookModals = Lists.newArrayList();
            List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByBookIssued(true);
            Set<Long> studentIdSet = Sets.newHashSet();
            studentBookMappings.forEach(student -> {
                studentIdSet.add(student.getStudentId());
            });
            studentIdSet.forEach(studentId -> {
                issuedBookModals.add(getBooksIssued(studentId));
            });
            return issuedBookModals;
        } catch (Exception e) {
            log.error("Error occurred while fetching all issued books with error message : {}", e.getMessage());
            throw responseStatusUtility
                    .internalServerErrorStatusException("Error occurred while fetching all issued books");
        }
    }

    public IssuedBookModalDTO getBooksIssued(long studentId) {
        List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId,
                true);
        List<BooksIssued> booksIssuedList = Lists.newArrayList();
        studentBookMappings.forEach(book -> {
            Books books = booksRepo.findById(book.getBookId()).get();
            BooksIssued booksIssued = BooksIssued.builder()
                    .bookName(books.getBookName())
                    .bookId(books.getId())
                    .bookUniqueId(books.getBookId())
                    .bookDepartment(books.getDepartment())
                    .bookAuthor(books.getBookAuthor()).build();
            booksIssuedList.add(booksIssued);
        });
        Students students = studentRepository.findById(studentId).get();
        return IssuedBookModalDTO.builder().studentId(students.getId())
                .studentName(students.getStudentName())
                .studentRollNo(students.getRollNo())
                .studentDepartment(students.getDepartment())
                .booksIssued(booksIssuedList).build();
    }

}
