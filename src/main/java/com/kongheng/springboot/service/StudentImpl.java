package com.kongheng.springboot.service;

import com.kongheng.springboot.model.Student;
import com.kongheng.springboot.supplier.StudentSupplier;
import org.springframework.stereotype.Service;

@Service
public class StudentImpl implements StudentService {

  @Override
  public Student getStudentById(Long studentId) {
    return StudentSupplier.students.get().stream()
        .filter(student -> studentId.equals(student.getStudentId()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Student "+ studentId));
  }
}
