package com.hospitalmanagement.hospitalmanagement.services.Impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hospitalmanagement.hospitalmanagement.entities.Patient;
import com.hospitalmanagement.hospitalmanagement.repositories.PatientRepository;
import com.hospitalmanagement.hospitalmanagement.services.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	PatientRepository patientRepository;

	@Override
	public List<Patient> getPatient() {
		return (List<Patient>) patientRepository.findAll();
	}

	@Override
	public Patient addPatient(Patient patient) {
		if (Objects.isNull(patient.getName()) || patient.getName().equals("") || patient.getName().equals(" ")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name can't be null or empty");
		} else if (String.format("%s", patient.getMobile()).length() < 10
				|| (String.format("%s", patient.getMobile()).length() > 10)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mobile number should be of 10 digit");
		} else if (patient.getAge() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Age should be greater than 0");
		}
		return patientRepository.save(patient);
	}

	@Override
	public Patient getPatientById(long id) {
		if (patientRepository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No record exists with id : %s", id));
		} else {
			return patientRepository.findById(id).get();
		}
	}

	@Override
	public String deletePatient(long id) {
		if (patientRepository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No record exists with id : %s so delete operation can't be performed", id));
		} else {
			patientRepository.deleteById(id);
			return "Patient deleted with id : " + id;
		}
	}

	@Override
	public Patient updatePatient(long id, Patient patient) {
		if (patientRepository.findById(id).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No record exists with id : %s", id));
		} else {
			patient.setId(id);
			return patientRepository.save(patient);
		}
	}

}
