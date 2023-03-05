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
@Table(name = "student_book_mapping")
@Getter
@Setter
public class StudentBookMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;

	@Column(name = "student_name")
	private long studentId;
	
	@Column(name = "book_id")
	public long bookId;
	
	@Column(name = "issued_date")
	public long issuedDate;
	
	@Column(name = "return_date")
	public long returnDate;
	
	@Column(name = "book_issued")
	public boolean bookIssued;

}
