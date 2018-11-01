package team.guest.tgbotty;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaRepositories("team.guest.tgbotty.dao")
public class TgBottyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBottyApplication.class, args);
    }
}
