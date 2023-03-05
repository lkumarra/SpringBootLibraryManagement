package com.hospitalmanagement.hospitalmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hospitalmanagement.hospitalmanagement.entities.Patient;
import com.hospitalmanagement.hospitalmanagement.services.PatientService;

@RestController
public class HospitalController {
	
	@Autowired
	PatientService patientService;
	
	@GetMapping("/home")
	public String home() {
		return "This is home Page";
	}
	
	
	@GetMapping("/patients")
	public List<Patient> getPatientsDetails(){
		return patientService.getPatient();
	}
	
	@PostMapping("/patient")
	public Patient addPatientDetails(@RequestBody Patient patient){
		return patientService.addPatient(patient);
	}
	
	@GetMapping("/patient/{id}")
	public Patient getPatientsDetailsById(@PathVariable("id") long id){
		return patientService.getPatientById(id);
	}
	
	
	@PutMapping("/patient/{id}")
	public Patient updatePatient(@PathVariable("id") long id, @RequestBody Patient patient) {
		return patientService.updatePatient(id, patient);
	}
	
	
	@DeleteMapping("/patient/{id}")
	public String deletePatientRecord(@PathVariable("id") long id) {
		return patientService.deletePatient(id);
	}
	

}
