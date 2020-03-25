package mops;

import mops.module.services.SuchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApplication.class, args);
    }

    @Autowired
    private SuchService suchService;

    @Bean
    ApplicationRunner init() {
        return args -> suchService.initIndex();
    }

}



