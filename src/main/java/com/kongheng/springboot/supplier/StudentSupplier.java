package com.kongheng.springboot.supplier;

import com.kongheng.springboot.model.Student;
import java.util.List;
import java.util.function.Supplier;

public class StudentSupplier {

  public static Supplier<List<Student>> students = () -> List.of(
   new Student(1L, "Kongheng Long"),
   new Student(2L, "James Bond"),
   new Student(3L, "Maria Jones")
  );
}
