package com.kob.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kob")
public class KobBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KobBackendApplication.class, args);
    }

}
