package com.hospitalmanagement.hospitalmanagement.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patient_address")
@Getter
@Setter
public class PatientAddress {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long addressId;
	private String address;
	private String district;
	private String state;
	private String country;
	private int pincode;

}
