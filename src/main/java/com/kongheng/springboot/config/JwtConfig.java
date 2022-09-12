package com.kongheng.springboot.config;

import com.google.common.net.HttpHeaders;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application.jwt")
@NoArgsConstructor
@Data
@Component
public class JwtConfig {

  private String secretKey;
  private String tokenPrefix;
  private int tokenExpirationAfterDay;

  public String getAuthorizationHeader() {
    return HttpHeaders.AUTHORIZATION;
  }
}
