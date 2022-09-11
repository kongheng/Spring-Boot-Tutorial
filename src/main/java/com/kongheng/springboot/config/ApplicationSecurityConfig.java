package com.kongheng.springboot.config;

import static com.kongheng.springboot.constant.ApplicationUserRole.STUDENT;

import com.kongheng.springboot.service.ApplicationUserService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ApplicationUserService applicationUserService;

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
        .formLogin()
          .loginPage("/login").permitAll()
          .defaultSuccessUrl("/courses", true)
          .passwordParameter("password")
          .usernameParameter("username")
        .and()
        .rememberMe()
          .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
          .key("somethingverysecured")
          .rememberMeParameter("remember-me")
        .and()
        .logout()
          .logoutUrl("/logout")
          .clearAuthentication(true)
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID", "remember-me")
          .logoutSuccessUrl("/login");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(applicationUserService);
    return provider;
  }

//  @Override
//  @Bean
//  protected UserDetailsService userDetailsService() {
//    UserDetails kongheng = User.builder()
//        .username("kongheng")
//        .password(passwordEncoder.encode("password"))
////        .roles(ADMIN.name())
//        .authorities(ADMIN.getGrantedAuthorities())
//        .build();
//    UserDetails linda = User.builder()
//        .username("linda")
//        .password(passwordEncoder.encode("password"))
////        .roles(STUDENT.name())
//        .authorities(STUDENT.getGrantedAuthorities())
//        .build();
//    UserDetails tom = User.builder()
//        .username("tom")
//        .password(passwordEncoder.encode("password"))
////        .roles(ADMIN_TRAINEE.name())
//        .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
//        .build();
//    return new InMemoryUserDetailsManager(
//        kongheng,
//        linda,
//        tom
//    );
//  }
}
