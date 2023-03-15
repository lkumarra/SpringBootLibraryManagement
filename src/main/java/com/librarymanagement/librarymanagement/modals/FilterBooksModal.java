package com.librarymanagement.librarymanagement.modals;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class FilterBooksModal {

    private String bookAuthor;

    private String bookName;

    private String department;

    private Operator operator;

    private boolean status;
}
