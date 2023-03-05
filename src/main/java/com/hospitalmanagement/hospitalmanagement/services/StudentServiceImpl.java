package com.hospitalmanagement.hospitalmanagement.services;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hospitalmanagement.hospitalmanagement.entities.Students;
import com.hospitalmanagement.hospitalmanagement.repositories.StudentsRepository;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	public StudentsRepository studentRespository;

	@Override
	public List<Students> getAllStudents() {
		return (List<Students>) studentRespository.findAll();
	}

	@Override
	public Students getStudentDetailsById(long id) {
		if (studentRespository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					String.format("No record found for studentId : %s", id));
		} else {
			return studentRespository.findById(id).get();
		}
	}

	@Override
	public Students addStudent(Students students) {
		if (Objects.isNull(students.getStudentName()) || students.getStudentName().equals("")
				|| students.getStudentName().equals(" ")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student name");
		} else if (Objects.isNull(students.getDepartment()) || students.getDepartment().equals("")
				|| students.getDepartment().equals(" ")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Department name");
		} else {
			students.setStatus(true);
			students.setCreatedAt(Instant.now().toEpochMilli());
			students.setUpdatedAt(Instant.now().toEpochMilli());
			if (studentRespository.findByRollNo(students.getRollNo()).isEmpty()) {
				return studentRespository.save(students);
			} else {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Student with given roll no already exist");
			}
		}
	}

	@Override
	public Students updateStudentDetails(long id, Students students) {
		if (studentRespository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with given id does not exist");
		} else if (Objects.isNull(students.getStudentName()) || students.getStudentName().equals("")
				|| students.getStudentName().equals(" ")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid student name");
		} else if (Objects.isNull(students.getDepartment()) || students.getDepartment().equals("")
				|| students.getDepartment().equals(" ")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Department name");
		} else {
			students.setUpdatedAt(Instant.now().toEpochMilli());
			return studentRespository.save(students);
		}
	}

	@Override
	public String inactiveStudent(long id) {
		if (studentRespository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student does not exist");
		} else {
			Students students = studentRespository.findById(id).get();
			students.setStatus(false);
			studentRespository.save(students);
			return String.format("Student with id %s is marked inactive", id);
		}
	}

}
