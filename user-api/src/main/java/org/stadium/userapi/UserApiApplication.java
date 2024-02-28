package org.stadium.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.stadium.userapi"})
@EntityScan(basePackages = {"org.stadium.corelib.domain","org.stadium.corelib.repo.custom"})
@EnableJpaRepositories(basePackages = {"org.stadium.corelib.repo"})
public class UserApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }

}
