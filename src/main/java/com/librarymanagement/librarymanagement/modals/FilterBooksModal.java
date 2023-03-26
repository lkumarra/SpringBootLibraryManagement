package com.librarymanagement.librarymanagement.modals;

import com.librarymanagement.librarymanagement.enums.BooksFieldsEnum;
import com.librarymanagement.librarymanagement.enums.OrderEnum;
import com.librarymanagement.librarymanagement.enums.SortingFields;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Getter
@Setter
@Validated
public class FilterBooksModal {
    List<Filters> filters;
    Sort sort;

    @Getter
    @Setter
    @Validated
    public static class Filters{
        private BooksFieldsEnum field;
        private String value;
    }

    @Getter
    @Setter
    public static class Sort{
        private SortingFields field;
        private OrderEnum order;
    }

}
