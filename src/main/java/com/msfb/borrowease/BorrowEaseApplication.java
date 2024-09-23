package com.msfb.borrowease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BorrowEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowEaseApplication.class, args);
    }

}
