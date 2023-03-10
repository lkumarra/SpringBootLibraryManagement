package com.librarymanagement.librarymanagement.constants;


public class StudentErrorMessages {

	public static final String INVALID_STUDENT_NAME = "Invalid student name.";
	public static final String INVALID_DEPARTMENT_NAME = "Invalid department name.";
	public static final String ROLL_NO_ALREADY_EXISTS = "Student with given roll no already exist.";
	public static final String STUDENT_ID_NOT_EXISTS = "Student with given id does not exist.";
	public static final String STUDENT_MARKED_INACTIVE = "Student with id %s is marked inactive.";
	public static final String STUDENT_DATA_INVALID = "Students Data is not valid.";
	public static final String DUPLICATE_STUDENT_ENTRY = "Duplicate entry of student with rollNo %s.";
	public static final String STUDENT_MORE_THAN_FIFTY = "Can not add more than 50 students at one batch.";
	public static final String STUDENT_NOT_EXISTS = "No record found for studentId : %s.";
	public static final String CAN_NOT_INACTIVE_STUDENT = "Student can not be marked as inactive as some books are already issued to student.";
	public static final String CAN_NOT_DELETE_STUDENT="Student can not be deleted as inactive as some books are already issued to student.";

}
