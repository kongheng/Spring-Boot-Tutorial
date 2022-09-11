package com.kongheng.springboot.service;

import static com.kongheng.springboot.constant.ApplicationUserRole.ADMIN;
import static com.kongheng.springboot.constant.ApplicationUserRole.ADMIN_TRAINEE;
import static com.kongheng.springboot.constant.ApplicationUserRole.STUDENT;

import com.google.common.collect.Lists;
import com.kongheng.springboot.auth.ApplicationUser;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
    return getApplicationUser().stream()
        .filter(applicationUser -> username.equals(applicationUser.getUsername()))
        .findFirst();
  }

  private List<ApplicationUser> getApplicationUser() {
    return Lists.newArrayList(
        new ApplicationUser(
            ADMIN.getGrantedAuthorities(),
            passwordEncoder.encode("password"),
            "kongheng",
            true,
            true,
            true,
            true
        ),
        new ApplicationUser(
            STUDENT.getGrantedAuthorities(),
            passwordEncoder.encode("password"),
            "linda",
            true,
            true,
            true,
            true
        ),
        new ApplicationUser(
            ADMIN_TRAINEE.getGrantedAuthorities(),
            passwordEncoder.encode("password"),
            "tom",
            true,
            true,
            true,
            true
        )
    );
  }
}
