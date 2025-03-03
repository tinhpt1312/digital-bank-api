package org.tinhpt.digital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DigitalBankApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankApiApplication.class, args);
    }

}
