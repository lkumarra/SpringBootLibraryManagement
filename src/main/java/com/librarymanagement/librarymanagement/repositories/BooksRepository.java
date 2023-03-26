package com.librarymanagement.librarymanagement.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.librarymanagement.librarymanagement.entities.Books;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
	
	List<Books> findBooksByBookId(String bookId);

	List<Books> findBooksByBookName(String bookName);

	List<Books> findBooksByDepartment(String department);

	List<Books> findBooksByBookAuthor(String bookAuthor);

	List<Books> findBooksByStatus(boolean status);

	List<Books> findBooksByBookNameAndBookAuthorAndDepartmentAndStatus(String bookName, String bookAuthor, String department, boolean status);

	List<Books> findBooksByBookNameOrBookAuthorOrDepartmentOrStatus(String bookName, String bookAuthor, String department, boolean status);

}
