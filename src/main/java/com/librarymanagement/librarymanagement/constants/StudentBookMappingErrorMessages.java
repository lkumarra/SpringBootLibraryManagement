package com.librarymanagement.librarymanagement.constants;

public class StudentBookMappingErrorMessages {

	public static final String STUDENT_NOT_EXISTS = "Student id does not exists in our records.";
	public static final String BOOK_NOT_EXISTS = "Book id does not exists in our records.";
	public static final String BOOK_ALREADY_ISSUED = "Given book id is already issued so can not issue again.";
	public static final String BOOK_NOT_ISSUED = "Given book id does not issued to any student.";
	public static final String BOOK_NOT_ISSUED_TO_STUDENT = "Book is not issued to student so can not return the book.";
	public static final String BOOK_NOT_ISSUED_TO_ANY_STUDENT = "Can not return the book as this book is not issued to student";

}
