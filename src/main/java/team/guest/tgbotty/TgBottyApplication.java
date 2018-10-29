package team.guest.tgbotty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class TgBottyApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TgBottyApplication.class, args);
    }
}
