package com.hospitalmanagement.hospitalmanagement.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.hospitalmanagement.hospitalmanagement.entities.StudentBookMapping;

public interface StudentBookMappingRepository extends CrudRepository<StudentBookMapping, Long>{
	
	public StudentBookMapping findByBookId(long bookId);
	
	public List<StudentBookMapping> findByStudentId(long studentId);
	
	public StudentBookMapping findByBookIdAndBookIssued(long bookId, boolean status);
	
	public List<StudentBookMapping> findByStudentIdAndBookIssued(long studentId, boolean status);
	
	public List<StudentBookMapping> findByBookIssued(boolean status);

}
