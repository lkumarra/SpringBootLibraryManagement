package com.librarymanagement.librarymanagement.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.librarymanagement.librarymanagement.dtos.IssuedBookModalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.librarymanagement.librarymanagement.entities.StudentBookMapping;
import com.librarymanagement.librarymanagement.services.StudentBookMappingService;

@Controller
@RequestMapping("/api/v1/managebook")
public class StudentBookController {

	@Autowired
	StudentBookMappingService studentBookMappingService;

	@PostMapping("/issueBook")
	@RolesAllowed({"LIBRARIAN","STUDENT"})
	public ResponseEntity<HashMap<String, Object>> issueBook(
			@Validated @RequestBody StudentBookMapping studentBookMapping) {
		return new ResponseEntity<HashMap<String, Object>>(studentBookMappingService.issueBook(studentBookMapping),
				HttpStatus.CREATED);
	}

	@GetMapping("/getIssuedBookDetails/{studentId}")
	@RolesAllowed({"LIBRARIAN","STUDENT"})
	public ResponseEntity<IssuedBookModalDTO> getAllBooksAssignedToStudent(@PathVariable long studentId) {
		return ResponseEntity.ok(studentBookMappingService.getAllIssuedBookToStudent(studentId));
	}

	@PostMapping("/returnBook")
	@RolesAllowed({"LIBRARIAN","STUDENT"})
	public ResponseEntity<HashMap<String, Object>> returnBook(
			@Validated @RequestBody StudentBookMapping studentBookMapping) {
		return new ResponseEntity<HashMap<String, Object>>(studentBookMappingService.returnBook(studentBookMapping),
				HttpStatus.OK);
	}

	@GetMapping("/getAllIssuedBooks")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<IssuedBookModalDTO>> getAllBooksIssuedToStudents() {
		return ResponseEntity.ok(studentBookMappingService.getAllBooksIssuedToStudents());
	}

}
