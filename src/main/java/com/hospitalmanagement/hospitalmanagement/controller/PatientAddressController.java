package com.hospitalmanagement.hospitalmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hospitalmanagement.hospitalmanagement.entities.PatientAddress;
import com.hospitalmanagement.hospitalmanagement.services.AddressService;

@Controller
public class PatientAddressController {
	
	@Autowired
	AddressService addressService;
	
	@GetMapping("/patient/address/{id}")
	public ResponseEntity<PatientAddress> getPatientAddressViaId(@PathVariable long id){
		return new ResponseEntity<PatientAddress>(addressService.getPatientAddress(id), HttpStatus.OK);
	}

}
