package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.librarymanagement.librarymanagement.constants.StudentErrorMessages;
import com.librarymanagement.librarymanagement.entities.Students;
import com.librarymanagement.librarymanagement.repositories.StudentBookMappingRepository;
import com.librarymanagement.librarymanagement.repositories.StudentsRepository;
import com.librarymanagement.librarymanagement.services.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	public StudentsRepository studentRespository;

	@Autowired
	public ResponseStatusUtility responseStatusUtility;

	@Autowired
	public StudentBookMappingRepository studentBookMapRepo;

	@Override
	public List<Students> getAllStudents() {
		try {
			return (List<Students>) studentRespository.findAll();
		} catch (Exception e) {
			log.error("Error occured while fetching the students with error message : {}", e.getMessage());
			throw responseStatusUtility.internalServerErrorStatusException("Error occured while getting the students");
		}
	}

	@Override
	public Students getStudentDetailsById(long id) {
		if (isUserIdExists(id))
			throw responseStatusUtility
					.notFoundStatusException(String.format(StudentErrorMessages.STUDENT_NOT_EXISTS, id));
		else {
			try {
				return studentRespository.findById(id).get();
			} catch (Exception e) {
				log.error("Error occured while getting the student with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while getting the student with id " + id);
			}
		}
	}

	public boolean isUserIdExists(long id) {
		return studentRespository.findById(id).isEmpty();
	}

	public boolean isStudentNameValid(Students students) {
		return (Objects.isNull(students.getStudentName()) || students.getStudentName().isEmpty()
				|| students.getStudentName().isBlank()) ? true : false;
	}

	public boolean isStudentDepartmentValid(Students students) {
		return (Objects.isNull(students.getDepartment()) || students.getDepartment().isEmpty()
				|| students.getDepartment().isBlank()) ? true : false;
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
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_STUDENT_NAME);
		else if (isStudentDepartmentValid(students))
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_DEPARTMENT_NAME);
		else {
			if (studentRespository.findByRollNo(students.getRollNo()).isEmpty()) {
				try {
					return saveStudentData(students);
				} catch (Exception e) {
					log.error("Error occured while adding the student with error message : {}", e.getMessage());
					throw responseStatusUtility
							.internalServerErrorStatusException("Error occured while adding the student");
				}
			} else
				throw responseStatusUtility.conflictStatusException(StudentErrorMessages.ROLL_NO_ALREADY_EXISTS);
		}
	}

	@Override
	public Students updateStudentDetails(long id, Students students) {
		if (isUserIdExists(id))
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
		else if (isStudentNameValid(students))
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_STUDENT_NAME);
		else if (isStudentDepartmentValid(students))
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_DEPARTMENT_NAME);
		else {
			try {
				students.setUpdatedAt(Instant.now().toEpochMilli());
				Students student = studentRespository.findById(id).get();
				students.setCreatedAt(student.getCreatedAt());
				return studentRespository.save(students);
			} catch (Exception e) {
				log.error("Error occured while updating the student with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while updating the student");
			}
		}

	}

	@Override
	public Map<String, String> inactiveStudent(long id) {
		Map<String, String> map = Maps.newConcurrentMap();
		if (studentRespository.findById(id).isEmpty())
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
		else if (!studentBookMapRepo.findByStudentIdAndBookIssued(id, true).isEmpty()) {
			throw responseStatusUtility.conflictStatusException(StudentErrorMessages.CAN_NOT_INACTIVE_STUDENT);
		} else {
			try {
				Students students = studentRespository.findById(id).get();
				students.setStatus(false);
				long updatedAt = Instant.now().toEpochMilli();
				students.setUpdatedAt(updatedAt);
				studentRespository.save(students);
				map.put("message", String.format(StudentErrorMessages.STUDENT_MARKED_INACTIVE, id));
				return map;
			} catch (Exception e) {
				log.error("Error occured while marking the student inactive with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while marking the studnet inactive");
			}
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
			throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_MORE_THAN_FIFTY);
		students.forEach(student -> {
			if (isStudentValid(student))
				studentsList.add(student);
			else
				throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_DATA_INVALID);
		});
		students.forEach(student -> {
			if (rollNo.contains(student.getRollNo()))
				throw responseStatusUtility.conflictStatusException(
						String.format(StudentErrorMessages.DUPLICATE_STUDENT_ENTRY, student.getRollNo()));
			else
				rollNo.add(student.getRollNo());
		});
		studentsList.forEach(student -> {
			try {
				studentListResponse.add(addStudent(student));
			} catch (Exception e) {
				log.error("Error occured while adding the students with error message : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while adding the students");
			}
		});
		return studentListResponse;
	}

	@Override
	public HashMap<String, String> deleteStudent(long studentId) {
		HashMap<String, String> studentBookHashMap = Maps.newHashMap();
		if (isUserIdExists(studentId))
			throw responseStatusUtility
					.notFoundStatusException(String.format(StudentErrorMessages.STUDENT_NOT_EXISTS, studentId));
		else if (!studentBookMapRepo.findByStudentIdAndBookIssued(studentId, true).isEmpty()) {
			throw responseStatusUtility.conflictStatusException(StudentErrorMessages.CAN_NOT_DELETE_STUDENT);
		} else {
			try {
				studentRespository.deleteById(studentId);
				studentBookHashMap.put("message", "Success");
				return studentBookHashMap;
			} catch (Exception e) {
				log.error("Error occured while deleting the student : {}", e.getMessage());
				throw responseStatusUtility
						.internalServerErrorStatusException("Error occured while deleting the studnet");
			}
		}
	}

}
