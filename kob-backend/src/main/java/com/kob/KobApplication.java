package com.kob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kob.mapper")
public class KobApplication {

    public static void main(String[] args) {
        SpringApplication.run(KobApplication.class, args);
    }

}
