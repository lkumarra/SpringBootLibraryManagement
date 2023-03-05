package com.hospitalmanagement.hospitalmanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Students {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public long Id;
	
	@Column(name = "student_name")
	private String studentName;
	
	@Column(name = "roll_no")
	private long rollNo;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "created_at")
	private long createdAt;
	
	@Column(name = "updated_at")
	private long updatedAt;
	
}
