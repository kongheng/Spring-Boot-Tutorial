package com.kongheng.springboot.service;

import com.kongheng.springboot.model.Student;
import com.kongheng.springboot.supplier.StudentSupplier;
import java.util.List;
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

  @Override
  public List<Student> getStudents() {
    return StudentSupplier.students.get();
  }

  @Override
  public void registerNewStudent(Student student) {
    System.out.println("Register new student => " + student);
  }

  @Override
  public void deleteStudent(Long studentId) {
    System.out.println("Delete student id = " + studentId);
  }

  @Override
  public void updateStudent(Long studentId, Student student) {
    System.out.printf("%s %s%n", studentId, student);
  }
}
