package com.kongheng.springboot.controller;

import com.kongheng.springboot.model.Student;
import com.kongheng.springboot.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

  @Autowired
  private StudentService studentService;

  @GetMapping(path = "{studentId}")
  public Student getStudent(@PathVariable("studentId") Long studentId) {
    return studentService.getStudentById(studentId);
  }
}
