package com.icceey.jweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.icceey.jweb.dao")
public class JwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwebApplication.class, args);
    }

}
