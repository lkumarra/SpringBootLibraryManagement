package com.librarymanagement.librarymanagement.services.Impl;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.librarymanagement.librarymanagement.modals.FilterStudentsModal;
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
    public StudentsRepository studentRepository;

    @Autowired
    public ResponseStatusUtility responseStatusUtility;

    @Autowired
    public StudentBookMappingRepository studentBookMapRepo;

    @Override
    public List<Students> getAllStudents() {
        try {
            return (List<Students>) studentRepository.findAll();
        } catch (Exception e) {
            log.error("Error occurred while fetching the students with error message : {}", e.getMessage());
            throw responseStatusUtility.internalServerErrorStatusException("Error occurred while getting the students");
        }
    }

    @Override
    public Students getStudentDetailsById(long id) {
        if (isUserIdExists(id))
            throw responseStatusUtility
                    .notFoundStatusException(String.format(StudentErrorMessages.STUDENT_NOT_EXISTS, id));
        else {
            try {
                return studentRepository.findById(id).get();
            } catch (Exception e) {
                log.error("Error occurred while getting the student with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while getting the student with id " + id);
            }
        }
    }

    public boolean isUserIdExists(long id) {
        return studentRepository.findById(id).isEmpty();
    }

    public boolean isStudentNameValid(Students students) {
        return Objects.isNull(students.getStudentName()) || students.getStudentName().isEmpty()
                || students.getStudentName().isBlank();
    }

    public boolean isStudentDepartmentValid(Students students) {
        return Objects.isNull(students.getDepartment()) || students.getDepartment().isEmpty()
                || students.getDepartment().isBlank();
    }

    private Students saveStudentData(Students students) {
        students.setStatus(true);
        students.setCreatedAt(Instant.now().toEpochMilli());
        students.setUpdatedAt(Instant.now().toEpochMilli());
        return studentRepository.save(students);
    }

    private void validateStudentData(Students students) {
        if (isStudentNameValid(students))
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_STUDENT_NAME);
        else if (isStudentDepartmentValid(students))
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_DEPARTMENT_NAME);
    }


    @Override
    public Students addStudent(Students students) {
        validateStudentData(students);
        if (studentRepository.findByRollNo(students.getRollNo()).isEmpty()) {
            try {
                return saveStudentData(students);
            } catch (Exception e) {
                log.error("Error occurred while adding the student with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while adding the student");
            }
        } else
            throw responseStatusUtility.conflictStatusException(StudentErrorMessages.ROLL_NO_ALREADY_EXISTS);

    }

    @Override
    public Students updateStudentDetails(long id, Students students) {
        if (isUserIdExists(id))
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
        else {
            validateStudentData(students);
            try {
                students.setUpdatedAt(Instant.now().toEpochMilli());
                Students student = studentRepository.findById(id).get();
                students.setCreatedAt(student.getCreatedAt());
                return studentRepository.save(students);
            } catch (Exception e) {
                log.error("Error occurred while updating the student with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while updating the student");
            }
        }

    }

    @Override
    public Map<String, String> inactiveStudent(long id) {
        Map<String, String> map = Maps.newConcurrentMap();
        if (studentRepository.findById(id).isEmpty())
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.STUDENT_ID_NOT_EXISTS);
        else if (!studentBookMapRepo.findByStudentIdAndBookIssued(id, true).isEmpty()) {
            throw responseStatusUtility.conflictStatusException(StudentErrorMessages.CAN_NOT_INACTIVE_STUDENT);
        } else {
            try {
                Students students = studentRepository.findById(id).get();
                students.setStatus(false);
                long updatedAt = Instant.now().toEpochMilli();
                students.setUpdatedAt(updatedAt);
                studentRepository.save(students);
                map.put("message", String.format(StudentErrorMessages.STUDENT_MARKED_INACTIVE, id));
                return map;
            } catch (Exception e) {
                log.error("Error occurred while marking the student inactive with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while marking the student inactive");
            }
        }
    }

    public boolean isStudentValid(Students students) {
        if (isStudentNameValid(students))
            return false;
        else if (isStudentDepartmentValid(students))
            return false;
        else if (studentRepository.findByRollNo(students.getRollNo()).isEmpty())
            return true;
        else
            return false;
    }

    @Override
    public List<Students> addStudents(List<Students> students) {
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
                log.error("Error occurred while adding the students with error message : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while adding the students");
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
                studentRepository.deleteById(studentId);
                studentBookHashMap.put("message", "Success");
                return studentBookHashMap;
            } catch (Exception e) {
                log.error("Error occurred while deleting the student : {}", e.getMessage());
                throw responseStatusUtility
                        .internalServerErrorStatusException("Error occurred while deleting the student");
            }
        }
    }

    @Override
    public List<Students> searchStudents(String students) {
        if (Objects.isNull(students) || students.isEmpty() || students.isBlank()) {
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_STUDENT_NAME);
        } else {
            try {
                List<Students> studentsList = (List<Students>) studentRepository.findAll();
                return studentsList.stream().filter(x -> x.getStudentName().contains(students)).collect(Collectors.toList());
            } catch (Exception e) {
                log.error("Error occurred while searching the student with error message : {}" + e.getMessage());
                throw responseStatusUtility.internalServerErrorStatusException("Error while searching the student");
            }
        }
    }

    @Override
    public List<Students> filterStudents(FilterStudentsModal filterStudentsModal) {
        if (Objects.isNull(filterStudentsModal.getStudentName()))
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_STUDENT_NAME);
        else if (Objects.isNull(filterStudentsModal.getDepartment()))
            throw responseStatusUtility.badRequestStatusException(StudentErrorMessages.INVALID_DEPARTMENT_NAME);
        else {
            switch (filterStudentsModal.getOperator()) {
                case and:
                    return studentRepository.findByStudentNameAndDepartmentAndRollNoAndStatus(filterStudentsModal.getStudentName(), filterStudentsModal.getDepartment(), filterStudentsModal.getRollNo(), filterStudentsModal.isStatus());
                case or:
                    return studentRepository.findByStudentNameOrDepartmentOrRollNoOrStatus(filterStudentsModal.getStudentName(), filterStudentsModal.getDepartment(), filterStudentsModal.getRollNo(), filterStudentsModal.isStatus());
                default:
                    return (List<Students>) studentRepository.findAll();
            }
        }
    }

}
