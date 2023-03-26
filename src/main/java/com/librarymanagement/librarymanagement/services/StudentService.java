package com.librarymanagement.librarymanagement.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.librarymanagement.librarymanagement.entities.Students;
import com.librarymanagement.librarymanagement.modals.FilterStudentsModal;

public interface StudentService {
	
	List<Students> getAllStudents();
	
	Students getStudentDetailsById(long id);
	
	Students addStudent(Students students);
	
	Students updateStudentDetails(long id, Students students);
	
	Map<String, String> inactiveStudent(long id);
	
	List<Students> addStudents(List<Students> students);
	
	HashMap<String, String> deleteStudent(long studentId);

	List<Students> searchStudents(String students);

	List<Students> filterStudents(int pageNo, int pageSize,FilterStudentsModal filterStudentsModal);
}
