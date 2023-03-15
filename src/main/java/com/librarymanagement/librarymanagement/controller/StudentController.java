package com.librarymanagement.librarymanagement.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.security.RolesAllowed;

import com.librarymanagement.librarymanagement.modals.FilterBooksModal;
import com.librarymanagement.librarymanagement.modals.FilterStudentsModal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.librarymanagement.librarymanagement.entities.Students;
import com.librarymanagement.librarymanagement.services.StudentService;

@Api(value = "student", description = "The students api", tags = {"students"})
@Controller
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    StudentService studentService;

    @ApiOperation(value = "Get all the students", nickname = "getStudents")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "return all students", response = Students.class, responseContainer = "List")})
    @GetMapping(value = "/getAllStudents", produces = {"application/json"})
    @RolesAllowed("LIBRARIAN")
    public ResponseEntity<List<Students>> getAllStudents() {
        return new ResponseEntity<List<Students>>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Student by id", nickname = "getStudentById")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "return the student", response = Students.class), @ApiResponse(code = 404, message = "Not found")})
    @GetMapping("/getStudent/{id}")
    @RolesAllowed({"LIBRARIAN", "STUDENT"})
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
        return ResponseEntity.ok(studentService.addStudents(students));
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

    @GetMapping("/searchStudent")
    @RolesAllowed("LIBRARIAN")
    public ResponseEntity<List<Students>> searchStudents(@RequestParam String studentName) {
        return ResponseEntity.ok(studentService.searchStudents(studentName));
    }

    @PostMapping("/filterStudents")
    @RolesAllowed("LIBRARIAN")
    public ResponseEntity<List<Students>> filterStudents(@RequestBody FilterStudentsModal filterStudentsModal) {
        return ResponseEntity.ok(studentService.filterStudents(filterStudentsModal));
    }

}
