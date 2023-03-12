package com.librarymanagement.librarymanagement.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.librarymanagement.entities.Books;

@Repository
public interface BooksRepository extends CrudRepository<Books, Long>{
	
	List<Books> findBooksByBookId(String bookId);

}
