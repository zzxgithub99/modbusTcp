package com.zx.modbustcp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModbusTcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModbusTcpApplication.class, args);
    }

}
