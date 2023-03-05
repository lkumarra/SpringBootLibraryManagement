package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.hospitalmanagement.hospitalmanagement.entities.StudentBookMapping;
import com.hospitalmanagement.hospitalmanagement.services.StudentBookMappingService;

@Controller
public class StudentBookController {

	@Autowired
	StudentBookMappingService studentBookMappingService;

	@PostMapping("/menu/issuebook")
	public ResponseEntity<HashMap<String, Object>> issueBook(
			@Validated @RequestBody StudentBookMapping studentBookMapping) {
		return new ResponseEntity<HashMap<String, Object>>(studentBookMappingService.issueBook(studentBookMapping),
				HttpStatus.CREATED);
	}

	@GetMapping("/menu/bookdetails/{studentId}")
	public ResponseEntity<HashMap<String, Object>> getAllBooksAssignedToStudent(@PathVariable long studentId) {
		return new ResponseEntity<HashMap<String, Object>>(
				studentBookMappingService.getAllIssuedBookToStudent(studentId), HttpStatus.OK);
	}

	@PostMapping("/menu/returnbook")
	public ResponseEntity<HashMap<String, Object>> returnBook(
			@Validated @RequestBody StudentBookMapping studentBookMapping) {
		return new ResponseEntity<HashMap<String, Object>>(studentBookMappingService.returnBook(studentBookMapping),
				HttpStatus.OK);
	}

	@GetMapping("/menu/allissuedbooks")
	public ResponseEntity<List<HashMap<String, Object>>> getAllBooksIssuedToStudents() {
		return new ResponseEntity<List<HashMap<String, Object>>>(
				studentBookMappingService.getAllBooksIssuedToStudents(), HttpStatus.OK);
	}

}
