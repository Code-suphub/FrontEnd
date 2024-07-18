package com.li;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.li.mapper")
public class ForumApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ForumApplication.class);
    }
}