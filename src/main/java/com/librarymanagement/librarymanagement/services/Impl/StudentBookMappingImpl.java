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
    private StudentBookMappingRepository studentBookMappingRepo;

    @Autowired
    private BooksRepository booksRepo;

    @Autowired
    private StudentsRepository studentRepository;

    @Autowired
    private ResponseStatusUtility responseStatusUtility;

    private boolean isStudentExistsAndActive(long studentId) {
        return studentRepository.existsById(studentId) &&
                studentRepository.findById(studentId).orElseThrow().isStatus();
    }

    private boolean isBookExistsAndActive(long bookId) {
        return booksRepo.existsById(bookId) &&
                booksRepo.findById(bookId).orElseThrow().isStatus();
    }

    private String issueBookToStudent(StudentBookMapping studentBookMapping) {
        long issuedDate = Instant.now().toEpochMilli();
        studentBookMapping.setIssuedDate(issuedDate);
        studentBookMapping.setBookIssued(true);
        studentBookMappingRepo.save(studentBookMapping);

        Students student = studentRepository.findById(studentBookMapping.getStudentId()).orElseThrow();
        Books book = booksRepo.findById(studentBookMapping.getBookId()).orElseThrow();

        String bookIssuedMessage = "Book '%s' with bookId '%s' is issued to student '%s' with roll no '%s'";
        return String.format(bookIssuedMessage, book.getBookName(), book.getBookId(),
                student.getStudentName(), student.getRollNo());
    }

    private String returnBookFromStudent(StudentBookMapping studentBookMapping) {
        StudentBookMapping issuedBook = studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
        long returnDate = Instant.now().toEpochMilli();

        studentBookMapping.setBookIssued(false);
        studentBookMapping.setId(issuedBook.getId());
        studentBookMapping.setReturnDate(returnDate);
        studentBookMapping.setIssuedDate(issuedBook.getIssuedDate());

        studentBookMappingRepo.save(studentBookMapping);

        Students student = studentRepository.findById(studentBookMapping.getStudentId()).orElseThrow();
        Books book = booksRepo.findById(studentBookMapping.getBookId()).orElseThrow();

        String bookReturnedMessage = "Book '%s' with bookId '%s' is returned from student '%s' with roll no '%s'";
        return String.format(bookReturnedMessage, book.getBookName(), book.getBookId(),
                student.getStudentName(), student.getRollNo());
    }

    @Override
    public HashMap<String, Object> issueBook(StudentBookMapping studentBookMapping) {
        HashMap<String, Object> hashMap = Maps.newHashMap();

        if (!isStudentExistsAndActive(studentBookMapping.getStudentId())) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.STUDENT_NOT_EXISTS);
        }

        if (!isBookExistsAndActive(studentBookMapping.getBookId())) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_EXISTS);
        }

        StudentBookMapping existingMapping = studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
        if (existingMapping != null && existingMapping.isBookIssued()) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_ALREADY_ISSUED);
        }

        try {
            hashMap.put("message", issueBookToStudent(studentBookMapping));
        } catch (Exception e) {
            log.error("Error occurred while issuing the book to student with error message: {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while issuing the book to student");
        }

        return hashMap;
    }

    @Override
    public HashMap<String, Object> returnBook(StudentBookMapping studentBookMapping) {
        HashMap<String, Object> hashMap = Maps.newHashMap();

        if (!isStudentExistsAndActive(studentBookMapping.getStudentId())) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.STUDENT_NOT_EXISTS);
        }

        if (!isBookExistsAndActive(studentBookMapping.getBookId())) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_EXISTS);
        }

        StudentBookMapping issuedBook = studentBookMappingRepo.findByBookIdAndBookIssued(studentBookMapping.getBookId(), true);
        if (issuedBook == null || !issuedBook.isBookIssued()) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_ISSUED);
        }

        if (issuedBook.getStudentId() != studentBookMapping.getStudentId()) {
            throw responseStatusUtility.badRequestStatusException(StudentBookMappingErrorMessages.BOOK_NOT_ISSUED_TO_STUDENT);
        }

        try {
            hashMap.put("message", returnBookFromStudent(studentBookMapping));
        } catch (Exception e) {
            log.error("Error occurred while returning the book from student with error message: {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while returning the book");
        }

        return hashMap;
    }

    @Override
    public IssuedBookModalDTO getAllIssuedBookToStudent(long studentId) {
        List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId, true);
        if (studentBookMappings.isEmpty()) {
            throw responseStatusUtility.notFoundStatusException("No issued book found for studentId " + studentId);
        }

        try {
            List<BooksIssued> booksIssuedList = Lists.newArrayList();
            for (StudentBookMapping mapping : studentBookMappings) {
                Books book = booksRepo.findById(mapping.getBookId()).orElseThrow();
                BooksIssued booksIssued = BooksIssued.builder()
                        .bookName(book.getBookName())
                        .bookId(book.getId())
                        .bookUniqueId(book.getBookId())
                        .bookDepartment(book.getDepartment())
                        .bookAuthor(book.getBookAuthor())
                        .build();
                booksIssuedList.add(booksIssued);
            }

            Students student = studentRepository.findById(studentId).orElseThrow();
            return IssuedBookModalDTO.builder()
                    .studentId(student.getId())
                    .studentName(student.getStudentName())
                    .studentRollNo(student.getRollNo())
                    .studentDepartment(student.getDepartment())
                    .booksIssued(booksIssuedList)
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while fetching the issued books to a student with error message: {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while fetching the issued books to a student");
        }
    }

    @Override
    public List<IssuedBookModalDTO> getAllBooksIssuedToStudents() {
        try {
            List<IssuedBookModalDTO> issuedBookModals = Lists.newArrayList();
            List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByBookIssued(true);
            Set<Long> studentIdSet = Sets.newHashSet();

            for (StudentBookMapping mapping : studentBookMappings) {
                studentIdSet.add(mapping.getStudentId());
            }

            for (Long studentId : studentIdSet) {
                issuedBookModals.add(getBooksIssued(studentId));
            }

            return issuedBookModals;
        } catch (Exception e) {
            log.error("Error occurred while fetching all issued books with error message: {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while fetching all issued books");
        }
    }

    public IssuedBookModalDTO getBooksIssued(long studentId) {
        List<StudentBookMapping> studentBookMappings = studentBookMappingRepo.findByStudentIdAndBookIssued(studentId, true);
        List<BooksIssued> booksIssuedList = Lists.newArrayList();

        for (StudentBookMapping mapping : studentBookMappings) {
            Books book = booksRepo.findById(mapping.getBookId()).orElseThrow();
            BooksIssued booksIssued = BooksIssued.builder()
                    .bookName(book.getBookName())
                    .bookId(book.getId())
                    .bookUniqueId(book.getBookId())
                    .bookDepartment(book.getDepartment())
                    .bookAuthor(book.getBookAuthor())
                    .build();
            booksIssuedList.add(booksIssued);
        }

        Students student = studentRepository.findById(studentId).orElseThrow();
        return IssuedBookModalDTO.builder()
                .studentId(student.getId())
                .studentName(student.getStudentName())
                .studentRollNo(student.getRollNo())
                .studentDepartment(student.getDepartment())
                .booksIssued(booksIssuedList)
                .build();
    }
}
