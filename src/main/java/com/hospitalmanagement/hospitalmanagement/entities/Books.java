package com.hospitalmanagement.hospitalmanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@Getter
@Setter
@ToString()
public class Books {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public long id;

	@Column(name = "book_id", nullable = false, unique = true)
	private String bookId;

	@Column(name = "book_name", nullable = false)
	private String bookName;

	@Column(name = "book_author", nullable = false)
	private String bookAuthor;

	@Column(name = "department", nullable = false)
	private String department;

	@Column(name = "status", nullable = false)
	private boolean status;

	@Column(name = "created_at", nullable = false)
	private long createdAt;

	@Column(name = "updated_at", nullable = false)
	private long updatedAt;

}
