package com.kongheng.springboot.service;

import com.kongheng.springboot.auth.ApplicationUser;
import java.util.Optional;

public interface ApplicationUserDao {

  Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
