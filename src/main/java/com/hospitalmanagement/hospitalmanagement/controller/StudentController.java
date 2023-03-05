package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hospitalmanagement.hospitalmanagement.entities.Students;
import com.hospitalmanagement.hospitalmanagement.services.StudentService;

@Controller
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/students")
	public ResponseEntity<List<Students>> getAllStudents(){
		return new ResponseEntity<List<Students>>(studentService.getAllStudents(), HttpStatus.OK);
	}
	
	@PostMapping("/students")
	public ResponseEntity<Students> addStudent(@Validated @RequestBody Students students){
		return new ResponseEntity<Students>(studentService.addStudent(students), HttpStatus.CREATED);
	}
	
}
