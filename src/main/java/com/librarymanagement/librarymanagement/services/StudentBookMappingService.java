package com.librarymanagement.librarymanagement.services;

import java.util.HashMap;
import java.util.List;

import com.librarymanagement.librarymanagement.dtos.IssuedBookModalDTO;
import com.librarymanagement.librarymanagement.entities.StudentBookMapping;

public interface StudentBookMappingService {
	
	public HashMap<String, Object> issueBook(StudentBookMapping studentBookMapping);
	
	public HashMap<String, Object> returnBook(StudentBookMapping studentBookMapping);
	
	public IssuedBookModalDTO getAllIssuedBookToStudent(long studentId);
	
	public List<IssuedBookModalDTO> getAllBooksIssuedToStudents();

}
