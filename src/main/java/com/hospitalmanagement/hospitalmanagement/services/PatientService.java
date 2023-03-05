package com.hospitalmanagement.hospitalmanagement.services;

import java.util.List;

import com.hospitalmanagement.hospitalmanagement.entities.Patient;

public interface PatientService {

	public List<Patient> getPatient();
	
	public Patient addPatient(Patient patient);
	
	public Patient getPatientById(long id);
	
	public String deletePatient(long id);
	
	public Patient updatePatient(long id, Patient patient);
	
}
