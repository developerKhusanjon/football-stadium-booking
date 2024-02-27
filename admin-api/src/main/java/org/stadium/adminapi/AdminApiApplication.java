package org.stadium.adminapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.stadium.adminapi"})
@EntityScan(basePackages = {"org.stadium.corelib.domain","org.stadium.corelib.repo.custom"})
@EnableJpaRepositories(basePackages = {"org.stadium.corelib.repo"})
public class AdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApiApplication.class, args);
    }

}
