package com.kongheng.springboot.config;

import static com.kongheng.springboot.constant.ApplicationUserRole.ADMIN;
import static com.kongheng.springboot.constant.ApplicationUserRole.ADMIN_TRAINEE;
import static com.kongheng.springboot.constant.ApplicationUserRole.STUDENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
//        .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//        .and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
        .antMatchers("/api/**").hasRole(STUDENT.name())
//        .antMatchers(DELETE, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
//        .antMatchers(POST, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
//        .antMatchers(PUT, "/management/api/**").hasAnyAuthority(COURSE_WRITE.getPermission())
//        .antMatchers(GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic();
  }

  @Override
  @Bean
  protected UserDetailsService userDetailsService() {
    UserDetails kongheng = User.builder()
        .username("kongheng")
        .password(passwordEncoder.encode("password"))
//        .roles(ADMIN.name())
        .authorities(ADMIN.getGrantedAuthorities())
        .build();
    UserDetails linda = User.builder()
        .username("linda")
        .password(passwordEncoder.encode("password"))
//        .roles(STUDENT.name())
        .authorities(STUDENT.getGrantedAuthorities())
        .build();
    UserDetails tom = User.builder()
        .username("tom")
        .password(passwordEncoder.encode("password"))
//        .roles(ADMIN_TRAINEE.name())
        .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
        .build();
    return new InMemoryUserDetailsManager(
        kongheng,
        linda,
        tom
    );
  }
}
