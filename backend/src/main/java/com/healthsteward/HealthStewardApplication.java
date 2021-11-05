package com.healthsteward;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.healthsteward.mapper")
public class HealthStewardApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthStewardApplication.class, args);
    }

}
