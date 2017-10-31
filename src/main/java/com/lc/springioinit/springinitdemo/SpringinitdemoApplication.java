package com.lc.springioinit.springinitdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringinitdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringinitdemoApplication.class, args);
    }
}
