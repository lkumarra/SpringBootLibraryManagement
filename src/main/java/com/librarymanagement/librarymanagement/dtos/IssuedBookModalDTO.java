package com.librarymanagement.librarymanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssuedBookModalDTO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long studentId;
    private String studentName;
    private long studentRollNo;
    private String studentDepartment;
    private List<BooksIssued> booksIssued;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public  static class BooksIssued{
        private long bookId;
        private String bookName;
        private String bookUniqueId;
        private String bookDepartment;
        private String bookAuthor;
    }

}
