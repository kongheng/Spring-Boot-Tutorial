package com.kongheng.springboot.controller;

import com.kongheng.springboot.model.Student;
import com.kongheng.springboot.service.StudentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {

  @Autowired
  private StudentService studentService;

//  hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

  @GetMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
  public List<Student> getAllStudent() {
    return studentService.getStudents();
  }

  @PostMapping
  @PreAuthorize("hasAuthority('student:write')")
  public void registerNewStudent(@RequestBody Student student) {
    studentService.registerNewStudent(student);
  }

  @DeleteMapping(path = "{studentId}")
  @PreAuthorize("hasAuthority('student:write')")
  public void deleteStudent(@PathVariable("studentId") Long studentId) {
    studentService.deleteStudent(studentId);
  }

  @PutMapping(path = "{studentId}")
  @PreAuthorize("hasAuthority('student:write')")
  public void updateStudent(@PathVariable("studentId") Long studentId,
      @RequestBody Student student) {
    studentService.updateStudent(studentId, student);
  }
}
