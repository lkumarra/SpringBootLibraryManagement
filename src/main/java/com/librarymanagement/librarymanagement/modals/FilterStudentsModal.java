package com.librarymanagement.librarymanagement.modals;

import com.librarymanagement.librarymanagement.enums.OrderEnum;
import com.librarymanagement.librarymanagement.enums.SortingFields;
import com.librarymanagement.librarymanagement.enums.StudentsFields;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Getter
@Setter
@Validated
public class FilterStudentsModal {

 List<Filters> filters;
 Sort sort;

 @Getter
 @Setter
 @Validated
 public static class Filters{
  private StudentsFields field;
  private String value;
 }

 @Getter
 @Setter
 @Validated
 public static class Sort{
  private SortingFields field;
  private OrderEnum order;
 }

}
