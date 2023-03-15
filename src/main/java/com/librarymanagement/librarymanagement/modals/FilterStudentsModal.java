package com.librarymanagement.librarymanagement.modals;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class FilterStudentsModal {

    private String studentName;

    private String department;

    private long rollNo;

    private Operator operator;

    private boolean status;

}
