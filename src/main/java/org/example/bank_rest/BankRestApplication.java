package org.example.bank_rest;

import org.example.bank_rest.properties.CorsProperties;
import org.example.bank_rest.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({
    CorsProperties.class,
    JwtProperties.class
})
public class BankRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankRestApplication.class, args);
    }

}
