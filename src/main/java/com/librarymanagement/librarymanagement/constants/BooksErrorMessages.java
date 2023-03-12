package com.librarymanagement.librarymanagement.constants;

public class BooksErrorMessages {

	public static final String BOOK_NOT_EXISTS = "No record exists for given id.";
	public static final String INVALID_BOOK_NAME ="Book name can not be null or empty.";
	public static final String INVALID_BOOK_AUTHOR = "Book author can not be null or empty.";
	public static final String INVALID_BOOK_DEPARTMENT = "Department can not be null or empty.";
	public static final String BOOK_ID_DUPLICATE = "Book Id should be unique given book id is already exists.";
	public static final String INVALID_BOOK_DATA = "Invalid book Data for book : %s";
	public static final String DUPLICATE_BOOK_IDS = "Duplicate entries with bookId :%s";
	public static final String CAN_NOT_INACTIVE_BOOK = "Book can not be marked as inactive as it is already issued to some student.";
	public static final String CAN_NOT_DELETE_BOOK = "Book can not be deleted as it is already issued to some student.";
	
}
