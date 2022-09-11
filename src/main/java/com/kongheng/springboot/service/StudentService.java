package com.kongheng.springboot.service;

import com.kongheng.springboot.model.Student;
import java.util.List;

public interface StudentService {

  Student getStudentById(Long studentId);

  List<Student> getStudents();

  void registerNewStudent(Student student);

  void deleteStudent(Long studentId);

  void updateStudent(Long studentId, Student student);
}
