package br.fiap.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PortalwebApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalwebApplication.class, args);
    }
}
