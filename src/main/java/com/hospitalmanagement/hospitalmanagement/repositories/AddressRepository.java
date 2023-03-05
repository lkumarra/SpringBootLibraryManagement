package com.hospitalmanagement.hospitalmanagement.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.hospitalmanagement.hospitalmanagement.entities.PatientAddress;

@Repository
public interface AddressRepository extends CrudRepository<PatientAddress, Long> {

}
