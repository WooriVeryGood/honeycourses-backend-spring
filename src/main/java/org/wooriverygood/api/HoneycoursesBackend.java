package org.wooriverygood.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HoneycoursesBackend {
    public static void main(String[]args) {
        SpringApplication.run(HoneycoursesBackend.class, args);
    }
}
