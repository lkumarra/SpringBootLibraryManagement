package com.hospitalmanagement.hospitalmanagement.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hospitalmanagement.hospitalmanagement.entities.Patient;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
	
}
