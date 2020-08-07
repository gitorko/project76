package com.demo.project76;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@RestController
@RefreshScope
@Slf4j
class HomeController{

    @Value("${check.flag}")
    private Boolean checkFlag;

    @GetMapping(value = "/greet")
    public String greet() {
        log.info("checkFlag: {}", checkFlag);
        return checkFlag ? "Good Morning" : "Good Bye";
    }
}
