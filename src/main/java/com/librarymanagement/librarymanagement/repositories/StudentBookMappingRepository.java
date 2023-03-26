package com.librarymanagement.librarymanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.librarymanagement.librarymanagement.entities.StudentBookMapping;

public interface StudentBookMappingRepository extends JpaRepository<StudentBookMapping, Long> {
	
	public StudentBookMapping findByBookId(long bookId);
	
	public List<StudentBookMapping> findByStudentId(long studentId);
	
	public StudentBookMapping findByBookIdAndBookIssued(long bookId, boolean status);
	
	public List<StudentBookMapping> findByStudentIdAndBookIssued(long studentId, boolean status);
	
	public List<StudentBookMapping> findByBookIssued(boolean status);


}
