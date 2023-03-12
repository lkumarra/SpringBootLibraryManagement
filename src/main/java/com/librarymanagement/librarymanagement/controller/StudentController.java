package com.librarymanagement.librarymanagement.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.librarymanagement.librarymanagement.entities.Students;
import com.librarymanagement.librarymanagement.services.StudentService;

@Controller
@RequestMapping("/api/v1/students")
public class StudentController {

	@Autowired
	StudentService studentService;

	@GetMapping("/getAllStudents")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Students>> getAllStudents() {
		return new ResponseEntity<List<Students>>(studentService.getAllStudents(), HttpStatus.OK);
	}

	@GetMapping("/getStudent/{id}")
	@RolesAllowed({ "LIBRARIAN", "STUDENT" })
	public ResponseEntity<Students> getStudentsById(@PathVariable long id) {
		return ResponseEntity.ok(studentService.getStudentDetailsById(id));
	}

	@PostMapping("/addStudent")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Students> addStudent(@Validated @RequestBody Students students) {
		return new ResponseEntity<Students>(studentService.addStudent(students), HttpStatus.CREATED);
	}

	@PostMapping("/addStudents")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<List<Students>> addStudents(@Validated @RequestBody List<Students> students) {
		return ResponseEntity.ok(studentService.addStudnents(students));
	}

	@PutMapping("/updateStudent/{id}")
	@RolesAllowed("STUDENT")
	public ResponseEntity<Students> updateStudents(@PathVariable long id, @Validated @RequestBody Students students) {
		return ResponseEntity.ok(studentService.updateStudentDetails(id, students));
	}

	@PutMapping("/inActiveStudent/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Map<String, String>> inactiveStudent(@PathVariable long id) {
		return ResponseEntity.ok(studentService.inactiveStudent(id));
	}

	@DeleteMapping("/deleteStudent/{id}")
	@RolesAllowed("LIBRARIAN")
	public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable long id) {
		return ResponseEntity.ok(studentService.deleteStudent(id));
	}
}
