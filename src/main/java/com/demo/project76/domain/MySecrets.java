package com.demo.project76.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("my-group")
public class MySecrets {
    String username;
    String password;
    String dbname;
}
