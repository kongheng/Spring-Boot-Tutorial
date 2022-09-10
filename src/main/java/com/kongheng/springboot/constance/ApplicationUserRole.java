package com.kongheng.springboot.constance;

import static com.kongheng.springboot.constance.ApplicationUserPermission.COURSE_READ;
import static com.kongheng.springboot.constance.ApplicationUserPermission.COURSE_WRITE;
import static com.kongheng.springboot.constance.ApplicationUserPermission.STUDENT_READ;
import static com.kongheng.springboot.constance.ApplicationUserPermission.STUDENT_WRITE;

import com.google.common.collect.Sets;
import java.util.Set;

public enum ApplicationUserRole {
  STUDENT(Sets.newHashSet()),
  ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

  private final Set<ApplicationUserPermission> permissions;

  ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
    this.permissions = permissions;
  }

  public Set<ApplicationUserPermission> getPermissions() {
    return permissions;
  }
}
