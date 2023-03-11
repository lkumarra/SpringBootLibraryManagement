package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hospitalmanagement.hospitalmanagement.entities.Students;
import com.hospitalmanagement.hospitalmanagement.services.StudentService;

@Controller
@RequestMapping("/api/v1/students")
public class StudentController {

	@Autowired
	StudentService studentService;

	@GetMapping("/getAllStudents")
	public ResponseEntity<List<Students>> getAllStudents() {
		return new ResponseEntity<List<Students>>(studentService.getAllStudents(), HttpStatus.OK);
	}

	@GetMapping("/getStudent/{id}")
	public ResponseEntity<Students> getStudentsById(@PathVariable long id) {
		return ResponseEntity.ok(studentService.getStudentDetailsById(id));
	}

	@PostMapping("/addStudent")
	public ResponseEntity<Students> addStudent(@Validated @RequestBody Students students) {
		return new ResponseEntity<Students>(studentService.addStudent(students), HttpStatus.CREATED);
	}

	@PostMapping("/addStudents")
	public ResponseEntity<List<Students>> addStudents(@Validated @RequestBody List<Students> students) {
		return ResponseEntity.ok(studentService.addStudnents(students));
	}

	@PutMapping("/updateStudent/{id}")
	public ResponseEntity<Students> updateStudents(@PathVariable long id, @Validated @RequestBody Students students) {
		return ResponseEntity.ok(studentService.updateStudentDetails(id, students));
	}

	@PutMapping("/inActiveStudent/{id}")
	public ResponseEntity<String> inactiveStudent(@PathVariable long id) {
		return ResponseEntity.ok(studentService.inactiveStudent(id));
	}
}
