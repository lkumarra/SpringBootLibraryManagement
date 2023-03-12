package com.librarymanagement.librarymanagement.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.librarymanagement.entities.Students;

@Repository
public interface StudentsRepository extends CrudRepository<Students, Long>{
	
	List<Students> findByRollNo(long rollNo);
}
