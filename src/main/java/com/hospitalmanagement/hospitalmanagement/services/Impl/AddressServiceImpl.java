package com.hospitalmanagement.hospitalmanagement.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospitalmanagement.hospitalmanagement.entities.PatientAddress;
import com.hospitalmanagement.hospitalmanagement.repositories.AddressRepository;
import com.hospitalmanagement.hospitalmanagement.services.AddressService;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private AddressRepository addressRepository;
	
	
	@Override
	public PatientAddress getPatientAddress(long id) {
		return addressRepository.findById(id).get();
	}

}
