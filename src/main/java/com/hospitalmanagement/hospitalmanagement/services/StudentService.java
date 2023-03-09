package com.hospitalmanagement.hospitalmanagement.services;

import java.util.List;

import com.hospitalmanagement.hospitalmanagement.entities.Students;

public interface StudentService {
	
	public List<Students> getAllStudents();
	
	public Students getStudentDetailsById(long id);
	
	public Students addStudent(Students students);
	
	public Students updateStudentDetails(long id, Students students);
	
	public String inactiveStudent(long id);
	
	public List<Students> addStudnents(List<Students> students);

}
