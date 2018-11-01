package team.guest.tgbotty;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TgBottyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TgBottyApplication.class, args);
    }
}
