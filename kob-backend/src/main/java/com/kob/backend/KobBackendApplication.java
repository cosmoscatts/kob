package com.kob.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kob.backend.mapper")
public class KobBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KobBackendApplication.class, args);
    }

}
