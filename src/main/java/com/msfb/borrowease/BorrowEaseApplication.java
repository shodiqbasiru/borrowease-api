package com.msfb.borrowease;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
        info = @Info(
                title = "BorrowEase API",
                version = "1.0",
                description = "BorrowEase API Documentation"
        )
)
public class BorrowEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BorrowEaseApplication.class, args);
    }

}
