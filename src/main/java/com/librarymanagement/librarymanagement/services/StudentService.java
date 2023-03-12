package com.librarymanagement.librarymanagement.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.librarymanagement.librarymanagement.entities.Students;

public interface StudentService {
	
	public List<Students> getAllStudents();
	
	public Students getStudentDetailsById(long id);
	
	public Students addStudent(Students students);
	
	public Students updateStudentDetails(long id, Students students);
	
	public Map<String, String> inactiveStudent(long id);
	
	public List<Students> addStudnents(List<Students> students);
	
	public HashMap<String, String> deleteStudent(long studentId);

}
