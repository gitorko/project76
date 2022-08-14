package com.demo.project76.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
@Slf4j
public class HomeController {

    @Value("${featureFlag}")
    private Boolean featureFlag;

    @GetMapping(value = "/greet")
    public String greet() {
        log.info("featureFlag: {}", featureFlag);
        return featureFlag ? "Good Morning" : "Good Bye";
    }
}
