package com.hospitalmanagement.hospitalmanagement.services.Impl;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.hospitalmanagement.hospitalmanagement.constants.StudentErrorMessages;
import com.hospitalmanagement.hospitalmanagement.entities.Students;
import com.hospitalmanagement.hospitalmanagement.repositories.StudentsRepository;
import com.hospitalmanagement.hospitalmanagement.services.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	public StudentsRepository studentRespository;

	@Autowired
	public ResponseStatusUtility responseStatusUtility;

	@Override
	public List<Students> getAllStudents() {
		return (List<Students>) studentRespository.findAll();
	}

	@Override
	public Students getStudentDetailsById(long id) {
		if (isUserIdExists(id))
			throw responseStatusUtility.responseStatusException(HttpStatus.NOT_FOUND,
					String.format("No record found for studentId : %s", id));
		else
			return studentRespository.findById(id).get();

	}

	public boolean isUserIdExists(long id) {
		return studentRespository.findById(id).isEmpty();
	}

	public boolean isStudentNameValid(Students students) {
		return (Objects.isNull(students.getStudentName()) || students.getStudentName().equals("")
				|| students.getStudentName().equals(" ")) ? true : false;
	}

	public boolean isStudentDepartmentValid(Students students) {
		return (Objects.isNull(students.getDepartment()) || students.getDepartment().equals("")
				|| students.getDepartment().equals(" ")) ? true : false;
	}

	private Students saveStudentData(Students students) {
		students.setStatus(true);
		students.setCreatedAt(Instant.now().toEpochMilli());
		students.setUpdatedAt(Instant.now().toEpochMilli());
		return studentRespository.save(students);
	}

	@Override
	public Students addStudent(Students students) {
		if (isStudentNameValid(students))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.INVALID_STUDENT_NAME);
		else if (isStudentDepartmentValid(students))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.INVALID_DEPARTMENT_NAME);
		else {
			if (studentRespository.findByRollNo(students.getRollNo()).isEmpty())
				return saveStudentData(students);
			else
				throw responseStatusUtility.responseStatusException(HttpStatus.CONFLICT,
						StudentErrorMessages.ROLL_NO_ALREADY_EXISTS);
		}
	}

	@Override
	public Students updateStudentDetails(long id, Students students) {
		if (isUserIdExists(id))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
		else if (isStudentNameValid(students))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.INVALID_STUDENT_NAME);
		else if (isStudentDepartmentValid(students))
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.INVALID_DEPARTMENT_NAME);
		else {
			students.setUpdatedAt(Instant.now().toEpochMilli());
			return studentRespository.save(students);
		}
	}

	@Override
	public String inactiveStudent(long id) {
		if (studentRespository.findById(id).isEmpty())
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
		else {
			Students students = studentRespository.findById(id).get();
			students.setStatus(false);
			studentRespository.save(students);
			return String.format(StudentErrorMessages.STUDENT_MARKED_INACTIVE, id);
		}
	}

	public boolean isStudentValid(Students students) {
		if (isStudentNameValid(students))
			return false;
		else if (isStudentDepartmentValid(students))
			return false;
		else if (studentRespository.findByRollNo(students.getRollNo()).isEmpty())
			return true;
		else
			return false;
	}

	@Override
	public List<Students> addStudnents(List<Students> students) {
		List<Students> studentsList = Lists.newArrayList();
		List<Students> studentListResponse = Lists.newArrayList();
		List<Long> rollNo = Lists.newArrayList();
		if (students.size() > 50)
			throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
					StudentErrorMessages.STUDENT_MORE_THAN_FIFTY);
		students.forEach(student -> {
			if (isStudentValid(student))
				studentsList.add(student);
			else
				throw responseStatusUtility.responseStatusException(HttpStatus.BAD_REQUEST,
						StudentErrorMessages.STUDENT_DATA_INVALID);
		});
		students.forEach(student -> {
			if (rollNo.contains(student.getRollNo()))
				throw responseStatusUtility.responseStatusException(HttpStatus.CONFLICT,
						String.format(StudentErrorMessages.DUPLICATE_STUDENT_ENTRY, student.getRollNo()));
			else
				rollNo.add(student.getRollNo());
		});
		studentsList.forEach(student -> {
			studentListResponse.add(addStudent(student));
		});
		return studentListResponse;
	}

}
