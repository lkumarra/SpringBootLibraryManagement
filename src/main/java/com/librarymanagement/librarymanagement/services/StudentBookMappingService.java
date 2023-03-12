package com.librarymanagement.librarymanagement.services;

import java.util.HashMap;
import java.util.List;

import com.librarymanagement.librarymanagement.entities.StudentBookMapping;

public interface StudentBookMappingService {
	
	
	public HashMap<String, Object> issueBook(StudentBookMapping studentBookMapping);
	
	public HashMap<String, Object> returnBook(StudentBookMapping studentBookMapping);
	
	public HashMap<String, Object> getAllIssuedBookToStudent(long studentId);
	
	public List<HashMap<String, Object>> getAllBooksIssuedToStudents();

}
